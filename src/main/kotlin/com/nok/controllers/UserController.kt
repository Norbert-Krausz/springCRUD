package com.nok.controllers

import com.nok.model.dto.AddressDTORequest
import com.nok.model.dto.AddressDTOResponse
import com.nok.model.dto.UserDTORequest
import com.nok.model.dto.UserDTOResponse
import com.nok.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/users")
class UserController(var userService: UserService) {

    @PostMapping("/create")
    fun createUser(@RequestBody newUser: UserDTORequest): UserDTOResponse {
        return userService.createUser(newUser)
    }

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
        return userService.updateUserAddress(id, updatedAddress )
    }

    @GetMapping("/{id}/addresses")
    fun getUserAddresses(@PathVariable id: Long): List<AddressDTOResponse> {
        return userService.getUserAddresses(id)
    }
}