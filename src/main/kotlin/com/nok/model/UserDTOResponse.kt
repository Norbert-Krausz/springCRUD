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
    var phoneNumber: String
)