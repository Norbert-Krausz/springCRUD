package com.nok.repositories

import jakarta.persistence.*

@Entity//(name = "address")
@Table(name= "address")
data class AddressEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long?,

    @Column(name = "streetName")
    var streetName: String,

    @Column(name = "streetNumber")
    var streetNumber: String,

    @Column(name = "city")
    var city: String,

    @Column(name = "postcode")
    var postcode: String,

    //@OneToOne(mappedBy = "address")
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity//? = null
)