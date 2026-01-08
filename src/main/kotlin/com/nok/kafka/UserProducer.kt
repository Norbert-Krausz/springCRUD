package com.nok.kafka

import com.nok.model.dto.UserDTORequest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

//@Component
//class UserProducer(
//    private val kafkaTemplate: KafkaTemplate<String, UserDTORequest>
//) {
//    fun sendCreateUserEvent(payload: UserDTORequest) {
//        kafkaTemplate.send(KafkaTopicConfig.USER_CREATE_TOPIC, payload.email, payload)
//    }
//}

@Component
class UserProtoProducer(
    private val kafkaTemplate: org.springframework.kafka.core.KafkaTemplate<String, com.google.protobuf.Message>
) {
    fun sendCreateUserCommand(cmd: com.nok.proto.UserCreateCommand) {
        kafkaTemplate.send(KafkaTopicConfig.USER_CREATE_TOPIC, cmd.email, cmd)
    }
}

