package com.nok

import com.nok.model.dto.UserDTORequest
import com.nok.model.dto.UserDTOResponse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

@SpringBootTest(
    classes = [MyApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CRUDtest(
    @Autowired var restTemplate: TestRestTemplate
) {

    var userId: Long = 0

    @Test
    fun createUser() {
        val userDTORequest = UserDTORequest("No", "Ro", "noro@test.com", "0944123456", 5, null)
        val result = this.restTemplate.postForEntity("/users/create", userDTORequest, UserDTOResponse::class.java)
        userId = result.body?.id!!
        assertTrue { result.body?.firstName.equals("No") }
        assertTrue { result.body?.lastName.equals("Ro") }
        assertTrue { result.body?.email.equals("noro@test.com") }
        assertTrue { result.body?.phoneNumber.equals("0944123456") }
    }

    @Test
    fun returnUserSuccessfully() {
        createUser()
        val result = this.restTemplate.getForEntity("/users/{id}", UserDTOResponse::class.java, userId)
        assertTrue { result.body?.firstName.equals("No") }
    }

    @Test
    fun deleteUserSuccessfully() {
        createUser()
        this.restTemplate.delete("/users/{id}", userId)
        val result = this.restTemplate.getForEntity("/users/{id}", String::class.java, userId)
        assertTrue { result.statusCode.equals(HttpStatus.NOT_FOUND) }
    }

    @Test
    fun putUserSuccessfully() {
        createUser()
        val userDTORequest = UserDTORequest("Nono", "Roro", "test2@test.com", "0944654321", 5, null)
        this.restTemplate.put("/users/{id}", userDTORequest, userId)
        val result = this.restTemplate.getForEntity("/users/{id}", UserDTOResponse::class.java, userId)
        assertTrue { result.statusCode.is2xxSuccessful }
        assertTrue { result.body?.firstName.equals("Nono") }
    }
}