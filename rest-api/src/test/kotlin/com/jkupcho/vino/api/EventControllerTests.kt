package com.jkupcho.vino.api

import com.jkupcho.vino.common.Event
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [EventController::class])
class EventControllerTests(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var rabbitTemplate: RabbitTemplate

    @Test
    fun `successfully posts and sends`() {
        mockMvc.perform(
                post("/event").content("""
                {
                    "type": "person",
                    "payload": {
                        "name": "vino"
                    }
                }
            """.trimIndent())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().`is`(200)
                )
        Mockito.verify(rabbitTemplate).convertAndSend("vino-queue", Event("person", mapOf(Pair("name", "vino"))))
    }

    @Test
    fun `bad request no type`() {
        mockMvc.perform(
                post("/event").content("""
                {
                    "payload": {
                        "name": "vino"
                    }
                }
            """.trimIndent())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().`is`(400)
                )
    }

    @Test
    fun `bad request no payload`() {
        mockMvc.perform(
                post("/event").content("""
                {
                    "type": "person"
                }
            """.trimIndent())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().`is`(400)
                )
    }

    @Test
    fun `bad request wrong header`() {
        mockMvc.perform(
                post("/event").content("""
                {
                    "type": "person"
                }
            """.trimIndent())
                        .contentType(MediaType.TEXT_PLAIN)
                )
                .andExpect(
                        status().`is`(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value())
                )
    }

    @Test
    fun `bad request garbage data`() {
        mockMvc.perform(
                post("/event").content("""
                aslkdfaklsdjfkl
            """.trimIndent())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(
                        status().`is`(HttpStatus.BAD_REQUEST.value())
                )
    }

}