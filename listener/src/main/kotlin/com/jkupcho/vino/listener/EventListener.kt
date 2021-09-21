package com.jkupcho.vino.listener

import com.jkupcho.vino.common.Event
import com.jkupcho.vino.listener.repo.EventEntity
import com.jkupcho.vino.listener.repo.EventRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@RabbitListener(queues = ["\${queue.name}"])
class EventListener(private val eventRepository: EventRepository, @Value("\${filter.type}") private val filter: String) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @RabbitHandler
    fun receive(event: Event) {
        if (event.type != filter) {
            logger.info(event.toString())
            val entity = EventEntity(eventType = event.type, eventPayload = event.payload)
            eventRepository.save(entity)
        } else {
            logger.info("filtered event")
        }
    }

}