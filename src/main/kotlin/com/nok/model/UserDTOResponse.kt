package com.nok.model

import com.fasterxml.jackson.annotation.JsonProperty

data class UserDTOResponse (
    @JsonProperty("id")
    var id: Long,
    @JsonProperty("firstName")
    var firstName: String,
    @JsonProperty("lastName")
    var lastName: String,
    @JsonProperty("email")
    var email: String,
    @JsonProperty("phoneNumber")
    var phoneNumber: String,
    //@JsonProperty("address")
    //var address: AddressDTOResponse
    @JsonProperty("currentAddress")
    val currentAddress: AddressDTOResponse?,
    @JsonProperty("pastAddresses")
    val pastAddresses: List<AddressDTOResponse>
)

data class AddressDTOResponse(
    @JsonProperty("streetName")
    var streetName: String,
    @JsonProperty("streetNumber")
    var streetNumber: String,
    @JsonProperty("city")
    var city: String,
    @JsonProperty("postcode")
    var postcode: String
)