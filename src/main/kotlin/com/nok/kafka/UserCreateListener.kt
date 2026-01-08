package com.nok.kafka

import com.nok.model.dto.AddressDTORequest
import com.nok.model.dto.UserDTORequest
import com.nok.proto.Address
import com.nok.service.UserService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

//@Component
//class UserCreateListener(
//    private val userService: UserService,
//) {
//    @KafkaListener(
//        topics = [KafkaTopicConfig.USER_CREATE_TOPIC],
//        groupId = "galactus")
//    fun onMessage(@Payload msg: UserDTORequest, @Header(KafkaHeaders.RECEIVED_PARTITION) partition: String) {
//        // Persist via existing service
//        userService.createUser(msg)
//        println("Partition: $partition")
//    }
//}

@Component
class UserCreateProtoListener(
    private val userService: UserService
) {
    private fun Address.toDto(): AddressDTORequest =
        AddressDTORequest(
            streetName = streetName,
            streetNumber = streetNumber,
            postcode = postcode,
            city = city,
        )

    @KafkaListener(
        topics = [KafkaTopicConfig.USER_CREATE_TOPIC],
        groupId = "galactus"
    )
    fun onMessage(
        msg: com.nok.proto.UserCreateCommand
    ) {
        val addrDto: AddressDTORequest? =
            if (msg.hasAddress()) msg.address.toDto() else null
        // map proto → existing DTO
        val req = UserDTORequest(
            firstName = msg.firstName.ifBlank { null },
            lastName = msg.lastName.ifBlank { null },
            email = msg.email,
            phoneNumber = msg.phoneNumber.ifBlank { null },
            yearsExperience = msg.yearsExperience,
            address = addrDto
        )
        userService.createUser(req)
    }
}



// JSON -> ProtoBuf <- / AVRO
// Kafka config - serializer / deserializer for proto