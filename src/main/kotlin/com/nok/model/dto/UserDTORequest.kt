package com.nok.model.dto

data class UserDTORequest (
    var firstName: String,
    var lastName: String,
    var email: String,
    var phoneNumber: String,
    var yearsExperience: Int,
    val address: AddressDTORequest?
)

data class AddressDTORequest (
    var streetName: String,
    var streetNumber: String,
    var city: String,
    var postcode: String
)
