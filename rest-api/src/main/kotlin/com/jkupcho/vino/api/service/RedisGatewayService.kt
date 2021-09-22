package com.jkupcho.vino.api.service

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisGatewayService(val redisTemplate: RedisTemplate<String, Int>) : GatewayService{

    private val logger = LoggerFactory.getLogger(javaClass)

    val key = "API_GATEWAY"
    val rate = 5

    override fun isRateLimited(): Boolean {
        val valueOps = redisTemplate.boundValueOps(key)
        var value = valueOps.get()

        if (value == null) {
            valueOps.set(1, Duration.ofMinutes(1))
        } else if (value < rate) {
            valueOps.set(++value, Duration.ofMinutes(1))
        } else {
            return true
        }
        return false
    }

}