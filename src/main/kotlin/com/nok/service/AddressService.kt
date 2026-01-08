package com.nok.service

import com.nok.model.AddressEntity
import com.nok.model.UserEntity
import com.nok.model.dto.AddressDTORequest
import com.nok.model.dto.AddressDTOResponse
import com.nok.model.dto.UserDTORequest
import com.nok.repositories.AddressRepository
import com.nok.repositories.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class AddressService (var userRepository: UserRepository, var addressRepository: AddressRepository) {

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

    //@Transactional(Transactional.TxType.REQUIRES_NEW)
    fun saveUserAddress (newUser: UserDTORequest, user: UserEntity): AddressEntity? {
        val a = newUser.address ?: return null
        val address = AddressEntity(
            id = null,
            streetName = a.streetName,
            streetNumber = a.streetNumber,
            city = a.city,
            postcode = a.postcode,
            user = user
        )
        return addressRepository.save(address)
    }
}
