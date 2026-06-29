package com.nok.client

import com.nok.client.generated.UsersApi
import com.nok.client.generated.model.AddressRequest
import com.nok.client.generated.model.UserRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

/**
 * End-to-end demo of the OpenAPI-generated Kotlin client calling the OpenAPI-generated
 * server interface. Both halves come from src/main/resources/openapi/users-api.yaml, so
 * this test proves the contract-first server and client agree.
 *
 * "Use this client at this URL" = point the generated [UsersApi] client at the running
 * server's base path (here, the random test port).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class GeneratedClientDemoTest {

    companion object {
        @Container
        @ServiceConnection
        val postgres = PostgreSQLContainer("postgres:latest").apply {
            withDatabaseName("testdb")
            withUsername("testuser")
            withPassword("testpass")
        }
    }

    @LocalServerPort
    var port: Int = 0

    private fun client() = UsersApi(basePath = "http://localhost:$port")

    @Test
    fun `generated client creates and reads a user against the generated server`() {
        val api = client()

        val created = api.createUser(
            UserRequest(
                email = "gordon@blackmesa.com",
                yearsExperience = 5,
                firstName = "Gordon",
                lastName = "Freeman",
                phoneNumber = "123456789",
                address = AddressRequest(
                    streetName = "Main",
                    streetNumber = "123",
                    city = "BlackMesa",
                    postcode = "00000",
                ),
            )
        )

        assertNotNull(created.id)
        assertEquals("Gordon", created.firstName)
        // 5 years -> Mid-level seniority, computed server-side
        assertEquals("M", created.seniority.value)

        val fetched = api.getUser(created.id)
        assertEquals(created.id, fetched.id)
        assertEquals("gordon@blackmesa.com", fetched.email)
        assertEquals("BlackMesa", fetched.address?.city)

        val addresses = api.getUserAddresses(created.id)
        assertEquals(1, addresses.size)
        assertEquals("BlackMesa", addresses.first().city)
    }
}
