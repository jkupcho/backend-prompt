package com.jkupcho.vino.api

import com.jkupcho.vino.api.service.GatewayService
import com.jkupcho.vino.api.service.RedisGatewayService
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.data.redis.core.BoundValueOperations
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

class RedisGatewayServiceTests {

    @Mock lateinit var redisTemplate: RedisTemplate<String, Int>
    @Mock lateinit var valueOps: BoundValueOperations<String, Int>
    lateinit var gatewayService: GatewayService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        gatewayService = RedisGatewayService(redisTemplate, 5, 10)
    }

    @Test
    fun `value null sets to 1 and duration is not rate limited`() {
        Mockito.`when`(redisTemplate.boundValueOps("API_GATEWAY")).thenReturn(valueOps)
        Mockito.`when`(valueOps.get()).thenReturn(null)

        val result = gatewayService.isRateLimited()
        assertFalse(result)

        Mockito.verify(valueOps).set(1, Duration.ofSeconds(10))
    }

    @Test
    fun `value 1 sets to 2 and duration`() {
        Mockito.`when`(redisTemplate.boundValueOps("API_GATEWAY")).thenReturn(valueOps)
        Mockito.`when`(valueOps.get()).thenReturn(1)

        val result = gatewayService.isRateLimited()
        assertFalse(result)

        Mockito.verify(valueOps).set(2, Duration.ofSeconds(10))
    }

    @Test
    fun `value at rate is rate limited`() {
        Mockito.`when`(redisTemplate.boundValueOps("API_GATEWAY")).thenReturn(valueOps)
        Mockito.`when`(valueOps.get()).thenReturn(5)

        val result = gatewayService.isRateLimited()
        assertTrue(result)
    }

    @Test
    fun `edge case is not rate limited`() {
        Mockito.`when`(redisTemplate.boundValueOps("API_GATEWAY")).thenReturn(valueOps)
        Mockito.`when`(valueOps.get()).thenReturn(4)

        val result = gatewayService.isRateLimited()
        assertFalse(result)

        Mockito.verify(valueOps).set(5, Duration.ofSeconds(10))
    }

}