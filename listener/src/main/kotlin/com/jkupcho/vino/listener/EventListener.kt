package com.jkupcho.vino.listener

import com.jkupcho.vino.common.Event
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
@RabbitListener(queues = ["\${queue.name}"])
class EventListener {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitHandler
    fun receive(event: Event) {
        logger.info(event.toString())
    }

}