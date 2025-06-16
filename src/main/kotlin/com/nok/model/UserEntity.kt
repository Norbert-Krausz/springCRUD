package com.nok.model

import com.nok.model.enums.UserSeniority
import jakarta.persistence.*

@Entity
@Table(name= "users")
public class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    // ***FOR MIGRATION PRACTICE***
    //var years: Int,
    var yearsExperience: Int,
    @Enumerated(EnumType.STRING)
    var seniority: UserSeniority,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true ,fetch = FetchType.LAZY)
    val addresses: List<AddressEntity> = mutableListOf()
)

