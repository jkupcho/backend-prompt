package com.jkupcho.vino.api

import org.springframework.amqp.core.Queue
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@SpringBootApplication
class RestApiApplication

fun main(args: Array<String>) {
    runApplication<RestApiApplication>(*args)
}

@Configuration
class MessageConfiguration(@Value("\${queue.name}") val queueName: String) {

    @Bean
    fun queue(): Queue {
        return Queue(queueName, false)
    }

    @Bean
    fun messageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }

}
