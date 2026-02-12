package com.nok.repositories

import com.nok.model.IdempotencyRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IdempotencyRecordRepository : JpaRepository <IdempotencyRecord, Long> {
    fun findByKey(idemKey: String): IdempotencyRecord?
}
