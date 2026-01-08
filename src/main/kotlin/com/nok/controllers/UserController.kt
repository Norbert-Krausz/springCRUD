package com.nok.controllers

//import com.nok.kafka.UserProducer
import com.nok.kafka.UserProtoProducer

import com.nok.model.dto.AddressDTORequest
import com.nok.model.dto.AddressDTOResponse
import com.nok.model.dto.UserDTORequest
import com.nok.model.dto.UserDTOResponse
import com.nok.service.AddressService
import com.nok.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UserController(var userService: UserService, var addressService: AddressService, var userProtoProducer: UserProtoProducer) { //var userProducer: UserProducer

    @PostMapping("/create")
    fun createUser(@RequestBody newUser: UserDTORequest): UserDTOResponse {
        return userService.createUser(newUser)
    }

    @PostMapping("/create/async-proto")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun createUserAsyncProto(@RequestBody req: UserDTORequest) {
        userProtoProducer.sendCreateUserCommand(req.toProto())
    }


//    // async creation via Kafka
//    @PostMapping("/create/async")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    fun createUserAsync(@RequestBody req: UserDTORequest) {
//        userProducer.sendCreateUserEvent(req)
//    }


    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserDTOResponse {
        return userService.getUser(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody updatedUser: UserDTORequest) {
        return userService.updateUser(id, updatedUser)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }

    @PutMapping("/{id}/address")
    fun updateUserAddress(@PathVariable id: Long, @RequestBody updatedAddress: AddressDTORequest) {
        return addressService.updateUserAddress(id, updatedAddress )
    }

    @GetMapping("/{id}/addresses")
    fun getUserAddresses(@PathVariable id: Long): List<AddressDTOResponse> {
        return addressService.getUserAddresses(id)
    }

    fun UserDTORequest.toProto(): com.nok.proto.UserCreateCommand =
        com.nok.proto.UserCreateCommand.newBuilder()
            .setEmail(email)
            .setFirstName(firstName ?: "")
            .setLastName(lastName ?: "")
            .setPhoneNumber(phoneNumber ?: "")
            .setYearsExperience(yearsExperience ?: 0)
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


}