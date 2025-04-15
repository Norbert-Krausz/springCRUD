package com.nok.repositories

import jakarta.persistence.*

@Entity
@Table(name= "address")
data class AddressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "streetName")
    var streetName: String,

    @Column(name = "streetNumber")
    var streetNumber: String,

    @Column(name = "city")
    var city: String,

    @Column(name = "postcode")
    var postcode: String,

    var isCurrent: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity
)