package com.nok.model

import jakarta.persistence.*

@Entity
@Table(name="idempotency_keys")
public class IdempotencyRecord (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,
    @Column(name = "idem_key", nullable = false)
    val key : String,
    @Column(name = "resource_id")
    var resourceId : Long? = null,
    @Column(name = "completed", nullable = false)
    var completed : Boolean = false,
)