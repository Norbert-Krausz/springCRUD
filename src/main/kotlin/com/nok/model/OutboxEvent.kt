package com.nok.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "outbox_events")
class OutboxEvent(
    @Id
    val id: UUID = UUID.randomUUID(),
    val payload: String,
    var processed: Boolean = false,
    val createdAt: Instant = Instant.now()
)