package com.nok.service

import com.nok.model.UserDTORequest
import com.nok.model.UserDTOResponse
import com.nok.repositories.UserEntity
import com.nok.repositories.UserRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(var repository: UserRepository) {

    fun createUser(newUser: UserDTORequest): UserDTOResponse {
        val save = repository.save(
            UserEntity(
                id = null,
                firstName = newUser.firstName,
                lastName = newUser.lastName,
                email = newUser.email,
                phoneNumber = newUser.phoneNumber
            )
        )
        return UserDTOResponse(id = save.id!!, firstName = save.firstName, lastName = save.lastName, email = save.email,
            phoneNumber = save.phoneNumber)
    }

    fun getUser(id: Long): UserDTOResponse? {
        return repository.findById(id)
            .map { UserDTOResponse(id = it.id!!, firstName = it.firstName, lastName = it.lastName,
                email = it.email, phoneNumber = it.phoneNumber) }
            .getOrNull()
    }

    fun updateUser(id: Long, updatedUser: UserDTORequest): UserDTOResponse? {
        return repository.findById(id).map {
            val save = repository.save(UserEntity(id = it.id, firstName = updatedUser.firstName, lastName = updatedUser.lastName,
                email = updatedUser.email, phoneNumber = updatedUser.phoneNumber))
            UserDTOResponse(id = save.id!!, firstName = save.firstName, lastName = save.lastName, email = save.email,
                phoneNumber = save.phoneNumber)
        }.orElseGet(null)
    }

    fun deleteUser(id: Long) {
        repository.deleteById(id)
    }
}