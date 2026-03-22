package com.nok.repositories

import com.nok.model.IdempotencyRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface IdempotencyRecordRepository : JpaRepository <IdempotencyRecord, String> {
    fun findByKey(idemKey: String): IdempotencyRecord?

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM idempotency_records WHERE created_at < NOW() - INTERVAL '1 minute'",
        nativeQuery = true
    )
    fun deleteOlderThanOneDay(): Int
}

@Component
class IdempotencyCleanupJob(
    private val repo: IdempotencyRecordRepository
) {
    @Scheduled(fixedDelay = 60_000)
    fun cleanup() {
        val deleted = repo.deleteOlderThanOneDay()
        if (deleted > 0) {
            println("Deleted $deleted expired idempotency keys")
        }
    }
}
