package com.nok.api.mappers

import com.nok.api.generated.model.AddressRequest
import com.nok.api.generated.model.AddressResponse
import com.nok.api.generated.model.UserRequest
import com.nok.api.generated.model.UserResponse
import com.nok.model.dto.AddressDTORequest
import com.nok.model.dto.AddressDTOResponse
import com.nok.model.dto.UserDTORequest
import com.nok.model.dto.UserDTOResponse

/**
 * Maps between the OpenAPI-generated models (com.nok.api.generated.model) and the
 * hand-written service DTOs (com.nok.model.dto). Keeping the mapping at the controller
 * boundary lets the service/persistence layers stay untouched while the HTTP contract
 * is driven entirely by users-api.yaml.
 */

fun UserRequest.toDto(): UserDTORequest =
    UserDTORequest(
        firstName = firstName,
        lastName = lastName,
        email = email,
        phoneNumber = phoneNumber,
        yearsExperience = yearsExperience,
        address = address?.toDto(),
    )

fun AddressRequest.toDto(): AddressDTORequest =
    AddressDTORequest(
        streetName = streetName,
        streetNumber = streetNumber,
        city = city,
        postcode = postcode,
    )

fun UserDTOResponse.toApi(): UserResponse =
    UserResponse(
        id = id,
        email = email,
        yearsExperience = yearsExperience,
        seniority = UserResponse.Seniority.forValue(seniority),
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        address = address?.toApi(),
    )

fun AddressDTOResponse.toApi(): AddressResponse =
    AddressResponse(
        streetName = streetName,
        streetNumber = streetNumber,
        city = city,
        postcode = postcode,
        isCurrent = isCurrent,
    )

fun UserDTORequest.toProto(): com.nok.proto.UserCreateCommand =
    com.nok.proto.UserCreateCommand.newBuilder()
        .setEmail(email)
        .setFirstName(firstName ?: "")
        .setLastName(lastName ?: "")
        .setPhoneNumber(phoneNumber ?: "")
        .setYearsExperience(yearsExperience)
        .apply {
            address?.let { a ->
                setAddress(
                    com.nok.proto.Address.newBuilder()
                        .setStreetName(a.streetName)
                        .setStreetNumber(a.streetNumber)
                        .setPostcode(a.postcode)
                        .setCity(a.city)
                        .build()
                )
            }
        }
        .build()
