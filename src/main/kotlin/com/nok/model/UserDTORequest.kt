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
    var phoneNumber: String
)