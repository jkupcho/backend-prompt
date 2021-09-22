package com.jkupcho.vino.api.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisGatewayService(val redisTemplate: RedisTemplate<String, Int>, @Value("\${rate.limit}") val rate: Int, @Value("\${rate.seconds}") val seconds: Long) : GatewayService {

    val key = "API_GATEWAY"

    override fun isRateLimited(): Boolean {
        val valueOps = redisTemplate.boundValueOps(key)
        var value = valueOps.get()

        if (value == null) {
            valueOps.set(1, Duration.ofSeconds(seconds))
        } else if (value < rate) {
            valueOps.set(++value, Duration.ofSeconds(seconds))
        } else {
            return true
        }
        return false
    }

}