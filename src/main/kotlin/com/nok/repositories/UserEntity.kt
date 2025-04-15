package com.nok.repositories

import jakarta.persistence.*

@Entity//(name = "user")
@Table(name= "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null,

    @Column(name = "firstName")
    var firstName: String,

    @Column(name = "lastName")
    var lastName: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "phoneNumber")
    var phoneNumber: String,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val addresses: List<AddressEntity> = mutableListOf()
)

