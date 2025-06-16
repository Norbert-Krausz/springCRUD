package com.nok.service

import com.nok.model.dto.AddressDTORequest
import com.nok.model.dto.AddressDTOResponse
import com.nok.model.dto.UserDTORequest
import com.nok.model.dto.UserDTOResponse
import com.nok.model.AddressEntity
import com.nok.repositories.AddressRepository
import com.nok.model.UserEntity
import com.nok.model.enums.UserSeniority
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
            // ***FOR MIGRATION PRACTICE***
            //years = newUser.yearsExperience,
            yearsExperience = newUser.yearsExperience,
            seniority = if (newUser.yearsExperience < 3) UserSeniority.J
            else if (newUser.yearsExperience > 9) UserSeniority.S
            else UserSeniority.M,
            addresses = mutableListOf()
        )
        val savedUser = userRepository.save(user)

        val address = AddressEntity(
            id = null,
            streetName = newUser.address!!.streetName,
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
            // ***FOR MIGRATION PRACTICE***
            //yearsExperience = savedUser.years,
            yearsExperience = savedUser.yearsExperience,
            seniority = if (savedUser.yearsExperience < 3) UserSeniority.J.toString()
                        else if (savedUser.yearsExperience > 9) UserSeniority.S.toString()
                        else UserSeniority.M.toString(),
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
                // ***FOR MIGRATION PRACTICE***
                //yearsExperience = user.years,
                yearsExperience = user.yearsExperience,
                seniority = user.seniority.toString(),
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

    fun updateUser(id: Long, updatedUser: UserDTORequest) {
        return userRepository.findById(id).map { user ->
            user.firstName = updatedUser.firstName
            user.lastName = updatedUser.lastName
            user.email = updatedUser.email
            user.phoneNumber = updatedUser.phoneNumber
            // ***FOR MIGRATION PRACTICE***
            //user.years = updatedUser.yearsExperience
            user.yearsExperience = updatedUser.yearsExperience
            val saved = userRepository.save(user)
        }.orElse(null)
    }

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