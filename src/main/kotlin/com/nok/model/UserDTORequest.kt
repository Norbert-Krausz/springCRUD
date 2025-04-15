package com.nok.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDTORequest (
    @JsonProperty("firstName")
    var firstName: String,
    @JsonProperty("lastName")
    var lastName: String,
    @JsonProperty("email")
    var email: String,
    @JsonProperty("phoneNumber")
    var phoneNumber: String,
    @JsonProperty("address")
    val address: AddressDTORequest
)

data class AddressDTORequest (
    @JsonProperty("streetName")
    var streetName: String,
    @JsonProperty("streetNumber")
    var streetNumber: String,
    @JsonProperty("city")
    var city: String,
    @JsonProperty("postcode")
    var postcode: String
)
