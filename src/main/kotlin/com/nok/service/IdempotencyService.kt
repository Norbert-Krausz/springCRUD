package com.nok.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.nok.model.IdempotencyRecord
import com.nok.model.dto.UserDTOResponse
import com.nok.repositories.IdempotencyRecordRepository
import org.springframework.stereotype.Service

@Service
class IdempotencyService(
    private val repo: IdempotencyRecordRepository,
    private val objectMapper: ObjectMapper
) {
    fun process(key: String, action: () -> UserDTOResponse): Pair<UserDTOResponse, Boolean> {
        val existing = repo.findById(key)

        if (existing.isPresent) {
            val cached = objectMapper.readValue(existing.get().responseBody, UserDTOResponse::class.java)
            return Pair(cached, false)  // false = duplicate
        }

        val result = action()
        val record = IdempotencyRecord(
            key = key,
            responseBody = objectMapper.writeValueAsString(result),
            statusCode = 201
        )
        repo.save(record)
        return Pair(result, true)  // true = new request
    }
}