package com.nok.users.service

import com.nok.model.dto.AddressDTORequest
import com.nok.model.dto.UserDTORequest
import com.nok.repositories.UserRepository
import com.nok.service.UserService
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
//import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    companion object {
        @Container
        @ServiceConnection
        val postgres = PostgreSQLContainer("postgres:latest").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpass")
        }
    }
//    companion object {
//        @Container
//        @ServiceConnection
//        var mySql = MySQLContainer("mysql:latest").apply {
//            withDatabaseName("testdb")
//            withUsername("testuser")
//            withPassword("testpass")
//        }
//    }

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `createUser saves and returns correct data`() {
        val request = UserDTORequest(
            firstName = "Gordon",
            lastName = "Freeman",
            email = "gf@example.com",
            phoneNumber = "123456789",
            yearsExperience = 5,
            address = AddressDTORequest(
                streetName = "Main",
                streetNumber = "123",
                city = "BlackMesa",
                postcode = "00000"
            )
        )

        val response = userService.createUser(request)

        assertNotNull(response.id)
        assertEquals("Gordon", response.firstName)
        assertEquals("Freeman", response.lastName)
        assertEquals("gf@example.com", response.email)
        assertEquals("BlackMesa", response.address?.city)

        val savedUser = userRepository.findById(response.id).get()
        assertEquals("Gordon", savedUser.firstName)
    }

    @Test
    @Transactional
    fun `getUser returns correct user`() {
        val request = UserDTORequest(
            firstName = "Alex",
            lastName = "Smith",
            email = "alex.smith@example.com",
            phoneNumber = "987654321",
            yearsExperience = 1,
            address = AddressDTORequest(
                streetName = "Second",
                streetNumber = "10B",
                city = "City17",
                postcode = "99999"
            )
        )
        val createdUser = userService.createUser(request)
        val fetchedUser = userService.getUser(createdUser.id)

        assertNotNull(fetchedUser)
        assertEquals("Alex", fetchedUser!!.firstName)
        assertEquals("City17", fetchedUser.address?.city)
    }
}