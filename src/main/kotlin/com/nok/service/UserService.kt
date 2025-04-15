package com.nok.service

import com.nok.model.AddressDTORequest
import com.nok.model.AddressDTOResponse
import com.nok.model.UserDTORequest
import com.nok.model.UserDTOResponse
import com.nok.repositories.AddressEntity
import com.nok.repositories.AddressRepository
import com.nok.repositories.UserEntity
import com.nok.repositories.UserRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class UserService(var userRepository: UserRepository, var addressRepository: AddressRepository) {

    fun createUser(newUser: UserDTORequest): UserDTOResponse {
        val user = UserEntity(
            id = null,
            firstName = newUser.firstName,
            lastName = newUser.lastName,
            email = newUser.email,
            phoneNumber = newUser.phoneNumber,
            addresses = mutableListOf()
        )
        val savedUser = userRepository.save(user)

        val address = AddressEntity(
            id = null,
            streetName = newUser.address.streetName,
            streetNumber = newUser.address.streetNumber,
            city = newUser.address.city,
            postcode = newUser.address.postcode,
            user = savedUser
        )
        val savedAddress = addressRepository.save(address)

        return UserDTOResponse(
            id = savedUser.id!!,
            firstName = savedUser.firstName,
            lastName = savedUser.lastName,
            email = savedUser.email,
            phoneNumber = savedUser.phoneNumber,
            address = AddressDTOResponse(
                streetName = savedAddress.streetName,
                streetNumber = savedAddress.streetNumber,
                city = savedAddress.city,
                postcode = savedAddress.postcode,
                isCurrent = true
            )
        )
    }

    fun getUser(id: Long): UserDTOResponse? {
        return userRepository.findById(id)
            .map { user ->
                val currentAddress = user.addresses.firstOrNull { it.isCurrent }
                    ?: throw IllegalStateException("No address for user with id: $id")

                UserDTOResponse(
                id = user.id!!,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                phoneNumber = user.phoneNumber,
                address = AddressDTOResponse(
                    streetName = currentAddress.streetName,
                    streetNumber = currentAddress.streetNumber,
                    city = currentAddress.city,
                    postcode = currentAddress.postcode,
                    isCurrent = currentAddress.isCurrent
                )
            )
        }.getOrNull()
    }

//    fun updateUser(id: Long, updatedUser: UserDTORequest): UserDTOResponse? {
//        return userRepository.findById(id).map {
//            val save = userRepository.save(UserEntity(
//                id = it.id,
//                firstName = updatedUser.firstName,
//                lastName = updatedUser.lastName,
//                email = updatedUser.email,
//                phoneNumber = updatedUser.phoneNumber,
//                address = AddressEntity(
//                    id = it.id,
//                    streetName = updatedUser.address.streetName,
//                    streetNumber = updatedUser.address.streetNumber,
//                    city = updatedUser.address.city,
//                    postcode = updatedUser.address.postcode
//                )
//            ))
//            UserDTOResponse(
//                id = save.id!!,
//                firstName = save.firstName,
//                lastName = save.lastName,
//                email = save.email,
//                phoneNumber = save.phoneNumber,
//                address = AddressDTOResponse(
//                    streetName = save.address.streetName,
//                    streetNumber = save.address.streetNumber,
//                    city = save.address.city,
//                    postcode = save.address.postcode
//                )
//            )
//        }.orElseGet(null)
//    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

    // *************************
    // ADDRESS
    // *************************

    fun updateUserAddress(userId: Long, newAddress: AddressDTORequest) {
        val user = userRepository.findById(userId).orElseThrow { Exception("User not found.") }

        // Mark old addresses as not current
        for (address in user.addresses) {
            address.isCurrent = false;
            addressRepository.save(address);
        }

        // Save the new address
        val address = AddressEntity(
            streetName = newAddress.streetName,
            streetNumber = newAddress.streetNumber,
            city = newAddress.city,
            postcode = newAddress.postcode,
            isCurrent = true,
            user = user
        )
        addressRepository.save(address)
    }

    fun getUserAddresses(userId: Long): List<AddressDTOResponse> {
        val user = userRepository.findById(userId).orElseThrow { Exception("User not found.") }

        return user.addresses.map { address ->
            AddressDTOResponse(
                streetName = address.streetName,
                streetNumber = address.streetNumber,
                city = address.city,
                postcode = address.postcode,
                isCurrent = address.isCurrent
            )
        }
    }
}