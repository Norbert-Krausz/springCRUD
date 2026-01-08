package com.nok.model

import jakarta.persistence.*

@Entity
@Table(name= "address")
public class AddressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val streetName: String,
    val streetNumber: String,
    val city: String,
    val postcode: String,
    var isCurrent: Boolean = true,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    val user: UserEntity
)