package com.jkupcho.vino.api

import com.jkupcho.vino.api.service.GatewayService
import com.jkupcho.vino.common.Event
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/event")
class EventController(val rabbitTemplate: RabbitTemplate, @Value("\${queue.name}") val queueName: String, val gatewayService: GatewayService) {

    @PostMapping
    fun postEvent(@Validated @RequestBody event: Event): ResponseEntity<Void> {
        if (gatewayService.isRateLimited()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build()
        }
        rabbitTemplate.convertAndSend(queueName, event)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): Map<String, String?> {
        val errors: MutableMap<String, String?> = HashMap()
        ex.bindingResult.allErrors.filterIsInstance<FieldError>().map { e: FieldError -> errors[e.field] = e.defaultMessage }
        return errors
    }

}