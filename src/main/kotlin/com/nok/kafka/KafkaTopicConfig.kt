package com.nok.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class KafkaTopicConfig {
    companion object {
        const val USER_CREATE_TOPIC = "user-create"
    }

    @Bean
    fun userCreateTopic(): NewTopic =
        TopicBuilder.name(USER_CREATE_TOPIC)
            .partitions(1)
            .replicas(1)
            .build()
}
