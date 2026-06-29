package com.nok.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.nok.model.dto.UserDTORequest
import com.nok.repositories.OutboxEventRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxPublisher(
    private val outboxEventRepository: OutboxEventRepository,
    private val userProducer: UserProducer,
    private val objectMapper: ObjectMapper
) {
    @Scheduled(fixedDelay = 120000)
    fun publishEvents() {
        val events = outboxEventRepository.findFirst10Unprocessed()
        events.forEach { event ->
            val payload = objectMapper.readValue(event.payload, UserDTORequest::class.java)
            userProducer.sendUserCreatedEvent(payload)
            event.processed = true
            outboxEventRepository.save(event)
        }
    }
}
