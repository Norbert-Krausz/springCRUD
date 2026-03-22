package com.nok.handlers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

class DuplicateIdempotencyKeyException : RuntimeException("Record with this key already exists")

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateIdempotencyKeyException::class)
    fun handleDuplicates(ex: DuplicateIdempotencyKeyException): ResponseEntity<Map<String, String>> {
        return ResponseEntity.status(409).body(mapOf("Duplicate" to ex.message!!))
    }
}