package com.nok.model.dto

data class UserDTOResponse(
    var id: Long,
    var firstName: String?,
    var lastName: String?,
    var email: String,
    var phoneNumber: String?,
    var yearsExperience: Int,
    var seniority: String,
    var address: AddressDTOResponse?
)

data class AddressDTOResponse(
    var streetName: String,
    var streetNumber: String,
    var city: String,
    var postcode: String,
    var isCurrent: Boolean
)