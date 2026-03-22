package com.nok.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name="idempotency_records")
public class IdempotencyRecord (
    @Id
    var key: String,
    var responseBody: String,
    var statusCode: Int,
    var createdAt: Instant = Instant.now()
)