package com.nbk.coded.bankingService

import com.nbk.coded.bankingService.user.UserRepository
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.security.crypto.password.PasswordEncoder
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import com.nbk.coded.bankingService.user.User
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UserRegistrationTest {

	@LocalServerPort
	private var port: Int = 0

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Autowired
	lateinit var userRepository: UserRepository

	@Autowired
	lateinit var passwordEncoder: PasswordEncoder

	data class RegisterUserDTO(val username: String, val password: String)

	companion object {
		@JvmStatic
		@BeforeAll
		fun setUp(@Autowired userRepository: UserRepository, @Autowired passwordEncoder: PasswordEncoder) {
			userRepository.deleteAll()

			val testUser = User(
				username = "coded",
				password = passwordEncoder.encode("joincoded")
			)
			userRepository.save(testUser)
		}
	}

	@Test
	fun `test user registration`() {
		val url = "http://localhost:$port/users/v1/register"

		val headers = HttpHeaders().apply {
			contentType = MediaType.APPLICATION_JSON
		}

		val body = RegisterUserDTO(username = "testuser123", password = "pass123")
		val request = HttpEntity(body, headers)

		val response = restTemplate.postForEntity(url, request, User::class.java)

		assertEquals(HttpStatus.OK, response.statusCode)

		assertNotNull(response.body?.id)

		assertEquals("testuser123", response.body?.username)
	}
}
