package com.nok.service

import com.nok.model.IdempotencyRecord
import com.nok.repositories.IdempotencyRecordRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class IdempotencyService(
    val idemRepository : IdempotencyRecordRepository
) {
    @Transactional
    fun saveOrGet(key: String): IdempotencyRecord {
        return try {
            idemRepository.save(IdempotencyRecord(key = key))
        } catch (_: DataIntegrityViolationException) {
            idemRepository.findByKey(key) ?: error("Conflict")
        }
    }

    @Transactional
    fun markCompleted(key: String, userId: Long) {
        val rec = idemRepository.findByKey(key) ?: return
        rec.resourceId = userId
        rec.completed = true
        idemRepository.save(rec)
    }
}