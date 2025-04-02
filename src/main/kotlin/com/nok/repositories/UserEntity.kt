package com.nok.repositories

import jakarta.persistence.*

@Entity//(name = "user")
@Table(name= "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long?,

    @Column(name = "firstName")
    var firstName: String,

    @Column(name = "lastName")
    var lastName: String,

    @Column(name = "email")
    var email: String,

    @Column(name = "phoneNumber")
    var phoneNumber: String,

    //@OneToOne(cascade = [CascadeType.ALL])
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var addresses: MutableList<AddressEntity> = mutableListOf()
)

//{
//    fun getCurrentAddress(): AddressEntity? {
//        return addresses.maxByOrNull { it.id ?: 0 } // Assuming highest ID is the latest
//    }
//}

