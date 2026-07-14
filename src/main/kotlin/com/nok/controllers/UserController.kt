package com.nok.controllers

import com.nok.api.generated.UsersApi
import com.nok.api.generated.model.AddressRequest
import com.nok.api.generated.model.AddressResponse
import com.nok.api.generated.model.UserRequest
import com.nok.api.generated.model.UserResponse
import com.nok.api.mappers.toApi
import com.nok.api.mappers.toDto
import com.nok.api.mappers.toProto
import com.nok.kafka.UserProducer
import com.nok.kafka.UserProtoProducer
import com.nok.service.AddressService
import com.nok.service.IdempotencyService
import com.nok.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * Contract-first controller: implements the OpenAPI-generated [UsersApi] interface.
 * All routing/validation annotations come from the generated interface (driven by
 * src/main/resources/openapi/users-api.yaml) — this class only overrides the methods
 * and delegates to the existing services.
 */
@RestController
class UserController(
    private val userService: UserService,
    private val addressService: AddressService,
    private val idemService: IdempotencyService,
    private val userProducer: UserProducer,
    private val userProtoProducer: UserProtoProducer,
) : UsersApi {

    override fun createUser(userRequest: UserRequest): ResponseEntity<UserResponse> =
        ResponseEntity.ok(userService.createUser(userRequest.toDto()).toApi())

    // Kafka (JSON): fire-and-forget, the listener persists the user
    override fun createUserAsync(userRequest: UserRequest): ResponseEntity<Unit> {
        userProducer.sendCreateUserEvent(userRequest.toDto())
        return ResponseEntity.accepted().build()
    }

    // Kafka (Protobuf): fire-and-forget via the proto producer
    override fun createUserAsyncProto(userRequest: UserRequest): ResponseEntity<Unit> {
        userProtoProducer.sendCreateUserCommand(userRequest.toDto().toProto())
        return ResponseEntity.accepted().build()
    }

    override fun createUserIdem(
        idempotencyKey: String,
        userRequest: UserRequest,
    ): ResponseEntity<UserResponse> {
        val (result, isNew) = idemService.process(idempotencyKey) {
            userService.createUser(userRequest.toDto())
        }
        val status = if (isNew) HttpStatus.CREATED else HttpStatus.OK
        return ResponseEntity.status(status).body(result.toApi())
    }

    override fun getUser(id: Long): ResponseEntity<UserResponse> =
        userService.getUser(id)?.let { ResponseEntity.ok(it.toApi()) }
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

    override fun updateUser(id: Long, userRequest: UserRequest): ResponseEntity<Unit> {
        userService.updateUser(id, userRequest.toDto())
        return ResponseEntity.noContent().build()
    }

    override fun deleteUser(id: Long): ResponseEntity<Unit> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    override fun updateUserAddress(id: Long, addressRequest: AddressRequest): ResponseEntity<Unit> {
        addressService.updateUserAddress(id, addressRequest.toDto())
        return ResponseEntity.noContent().build()
    }

    override fun getUserAddresses(id: Long): ResponseEntity<List<AddressResponse>> =
        ResponseEntity.ok(addressService.getUserAddresses(id).map { it.toApi() })
}
