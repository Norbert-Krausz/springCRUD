package com.nok.repositories

import jakarta.persistence.*

@Entity(name = "user")
@Table(name= "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long?,

    @Column(name = "firstName")
    var firstName: String,

    @Column(name = "lastName")
    var lastName: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "phoneNumber")
    var phoneNumber: String
)

