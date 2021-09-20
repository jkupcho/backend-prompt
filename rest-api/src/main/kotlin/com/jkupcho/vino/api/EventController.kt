package com.jkupcho.vino.api

import com.jkupcho.vino.common.Event
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/event")
class EventController(val rabbitTemplate: RabbitTemplate, @Value("\${queue.name}") val queueName: String) {

    @PostMapping
    fun postEvent(@RequestBody event: Event) {
        rabbitTemplate.convertAndSend(queueName, event)
    }

}