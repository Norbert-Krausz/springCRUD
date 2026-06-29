package com.nok.repositories

import com.nok.model.OutboxEvent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface OutboxEventRepository : JpaRepository<OutboxEvent, UUID> {
    @Query("SELECT e FROM OutboxEvent e WHERE e.processed = false ORDER BY e.createdAt ASC LIMIT 10")
    fun findFirst10Unprocessed(): List<OutboxEvent>
}