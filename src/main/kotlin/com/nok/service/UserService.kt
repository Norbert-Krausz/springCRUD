package com.nok.service

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
        val userEntity = userRepository.save(
            UserEntity(
                id = null,
                firstName = newUser.firstName,
                lastName = newUser.lastName,
                email = newUser.email,
                phoneNumber = newUser.phoneNumber,
                addresses = mutableListOf()
//                    AddressEntity(
//                    id = null,
//                    streetName = newUser.address.streetName,
//                    streetNumber = newUser.address.streetNumber,
//                    city = newUser.address.city,
//                    postcode = newUser.address.postcode
//                    )
                )
            )

        val addressEntity = AddressEntity(
            id = null,
            streetName = newUser.address.streetName,
            streetNumber = newUser.address.streetNumber,
            city = newUser.address.city,
            postcode = newUser.address.postcode,
            user = userEntity
            )
        val savedAddress = addressRepository.save(addressEntity)

        // Update user with the new address
        userEntity.addresses.add(savedAddress)
        userRepository.save(userEntity)


        return UserDTOResponse(
            id = userEntity.id!!,
            firstName = userEntity.firstName,
            lastName = userEntity.lastName,
            email = userEntity.email,
            phoneNumber = userEntity.phoneNumber,
            currentAddress = AddressDTOResponse(
                streetName = savedAddress.streetName,
                streetNumber = savedAddress.streetNumber,
                city = savedAddress.city,
                postcode = savedAddress.postcode
            ),
            pastAddresses = listOf()
        )
    }

//    fun getUser(id: Long): UserDTOResponse? {
//        return userRepository.findById(id)
//            .map { UserDTOResponse(
//                id = it.id!!,
//                firstName = it.firstName,
//                lastName = it.lastName,
//                email = it.email,
//                phoneNumber = it.phoneNumber,
//                currentAddress = AddressDTOResponse(
//                    streetName = it.addresses[0].streetName,
//                    streetNumber = it.address.streetNumber,
//                    city = it.address.city,
//                    postcode = it.address.postcode
//                ),
//                pastAddresses = listOf()
//            )
//        }.getOrNull()
//    }
//
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
//
//    fun deleteUser(id: Long) {
//        userRepository.deleteById(id)
//    }
}