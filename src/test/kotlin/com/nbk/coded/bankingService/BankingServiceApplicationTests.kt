package com.nbk.coded.bankingService

import com.nbk.coded.bankingService.account.*
import com.nbk.coded.bankingService.kyc.KYC
import com.nbk.coded.bankingService.kyc.KycController
import com.nbk.coded.bankingService.security.AuthenticationResponse
import com.nbk.coded.bankingService.security.JwtService
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankingServiceApplicationTests {

	@LocalServerPort
	private var port: Int = 0

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Autowired
	lateinit var jwtService: JwtService

	private lateinit var headers: HttpHeaders
	private var defaultUserId: Long? = null
	private lateinit var account1Number: String
	private lateinit var account2Number: String

	private lateinit var kycTestValues: KYC


	private fun baseUrl(path: String) = "http://localhost:$port$path"

	@BeforeAll
	fun setUp() {
		val userRegistrationHeaders = HttpHeaders().apply {
			set("Content-Type", "application/json")
		}
		val user = mapOf("username" to "defaultuser", "password" to "password123")
		val registrationRequest = HttpEntity(user, userRegistrationHeaders)
		val registrationResponse = restTemplate.exchange(
			baseUrl("/users/v1/register"),
			HttpMethod.POST,
			registrationRequest,
			Map::class.java
		)

		defaultUserId = (registrationResponse.body?.get("id") as Int).toLong()
		Assertions.assertNotNull(defaultUserId, "Default user registration failed")

		val loginRequest = mapOf("username" to "defaultuser", "password" to "password123")
		val loginHeaders = HttpHeaders().apply {
			set("Content-Type", "application/json")
		}
		val loginEntity = HttpEntity(loginRequest, loginHeaders)
		val loginResponse = restTemplate.exchange(
			baseUrl("/auth/login"),
			HttpMethod.POST,
			loginEntity,
			AuthenticationResponse::class.java
		)

		val token = loginResponse.body?.jwt
		Assertions.assertNotNull(token, "Login failed, no token received")

		headers = HttpHeaders().apply {
			set("Content-Type", "application/json")
			set("Authorization", "Bearer $token")
		}


		val kycTestDTO = KycController.KycDTO(
			userId = defaultUserId!!,
			dob = "1990-01-01",
			nationality = "Kuwaiti",
			salary = BigDecimal(1500)
		)
		val kycRequest = HttpEntity(kycTestDTO, headers)

		println("Sending KYC request for user ID: $defaultUserId")

		val kycResponse = restTemplate.exchange(
			baseUrl("/users/v1/kyc"),
			HttpMethod.POST,
			kycRequest,
			KYC::class.java
		)

		println("KYC Response Status: ${kycResponse.statusCode}")
		println("KYC Response Body: ${kycResponse.body}")

		Assertions.assertEquals(HttpStatus.CREATED, kycResponse.statusCode, "Expected status code to be CREATED")

		this.kycTestValues = kycResponse.body ?: throw AssertionError("KYC creation failed")


		val account1 = AccountController.CreateAccountDTO(
			userId = defaultUserId!!,
			initialBalance = BigDecimal(100),
			accountType = AccountType.SAVINGS,
			name = "Test"
		)

		val account2 = AccountController.CreateAccountDTO(
			userId = defaultUserId!!,
			initialBalance = BigDecimal(200),
			accountType = AccountType.CHECKING,
			name = "Test"
		)

		val request1 = HttpEntity(account1, headers)
		val response1 = restTemplate.exchange(
			baseUrl("/accounts/v1/accounts"),
			HttpMethod.POST,
			request1,
			Account::class.java
		)
		Assertions.assertEquals(HttpStatus.OK, response1.statusCode)
		account1Number = response1.body?.accountNumber ?: throw AssertionError("Account 1 creation failed")

		val request2 = HttpEntity(account2, headers)
		val response2 = restTemplate.exchange(
			baseUrl("/accounts/v1/accounts"),
			HttpMethod.POST,
			request2,
			Account::class.java
		)
		Assertions.assertEquals(HttpStatus.OK, response2.statusCode)
		account2Number = response2.body?.accountNumber ?: throw AssertionError("Account 2 creation failed")

	}

	@Test
	fun `register a user`() {
		val user = mapOf(
			"username" to "testuser1",
			"password" to "password123"
		)
		val request = HttpEntity(user, headers)
		val response = restTemplate.exchange(
			baseUrl("/users/v1/register"),
			HttpMethod.POST,
			request,
			Map::class.java
		)

		Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
		val newUserId = (response.body?.get("id") as Int).toLong()
		Assertions.assertNotNull(newUserId, "New user registration failed")
	}

	@Test
	fun `create multiple accounts for default user`() {
		val account1 = AccountController.CreateAccountDTO(
			userId = defaultUserId!!,
			initialBalance = BigDecimal(100),
			accountType = AccountType.SAVINGS,
			name = "Test"
		)

		val account2 = AccountController.CreateAccountDTO(
			userId = defaultUserId!!,
			initialBalance = BigDecimal(200),
			accountType = AccountType.CHECKING,
			name = "Test"
		)

		val request1 = HttpEntity(account1, headers)
		val response1 = restTemplate.exchange(
			baseUrl("/accounts/v1/accounts"),
			HttpMethod.POST,
			request1,
			Account::class.java
		)

		Assertions.assertEquals(HttpStatus.OK, response1.statusCode)
		val account1Number = response1.body?.accountNumber
		Assertions.assertNotNull(account1Number, "Account 1 creation failed")

		val request2 = HttpEntity(account2, headers)
		val response2 = restTemplate.exchange(
			baseUrl("/accounts/v1/accounts"),
			HttpMethod.POST,
			request2,
			Account::class.java
		)

		Assertions.assertEquals(HttpStatus.OK, response2.statusCode)
		val account2Number = response2.body?.accountNumber
		Assertions.assertNotNull(account2Number, "Account 2 creation failed")
	}


	@Test
	fun `read list of accounts for default user`() {
		val request = HttpEntity<String>(headers)

		val response = restTemplate.exchange(
			baseUrl("/accounts/v1/accounts/user/{userId}"),
			HttpMethod.GET,
			request,
			List::class.java,
			defaultUserId
		)

		Assertions.assertEquals(HttpStatus.OK, response.statusCode)
		Assertions.assertFalse(response.body.isNullOrEmpty(), "Account list is empty")
		println("Response Body: ${response.body}")

	}

	@Test
	fun `default user can create KYC`() {
		val kyc = mapOf(
			"userId" to defaultUserId,
			"dob" to "1990-01-01",
			"nationality" to "Kuwaiti",
			"salary" to BigDecimal(1500)
		)

		val request = HttpEntity(kyc, headers)

		val response = restTemplate.exchange(
			baseUrl("/users/v1/kyc"),
			HttpMethod.POST,
			request,
			Map::class.java
		)

		Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
		Assertions.assertEquals(defaultUserId, (response.body?.get("id") as Int).toLong())
		Assertions.assertEquals("Kuwaiti", response.body?.get("nationality"))
	}

	@Test
	fun `default user can update KYC`() {
		val updatedKyc = mapOf(
			"userId" to defaultUserId,
			"dob" to "2003-03-05",
			"nationality" to "American",
			"salary" to BigDecimal(2000)
		)

		val request = HttpEntity(updatedKyc, headers)

		val response = restTemplate.exchange(
			baseUrl("/users/v1/kyc"),
			HttpMethod.POST,
			request,
			Map::class.java
		)

		Assertions.assertEquals(HttpStatus.CREATED, response.statusCode)
		Assertions.assertEquals("American", response.body?.get("nationality"))
	}

	@Test
	fun `default user can read KYC`() {
		val request = HttpEntity<String>(headers)

		val response = restTemplate.exchange(
			baseUrl("/users/v1/kyc/{userId}"),
			HttpMethod.GET,
			request,
			KYC::class.java,
			defaultUserId
		)

		Assertions.assertEquals(HttpStatus.OK, response.statusCode, "Expected status code to be OK")

		val kycResponse = response.body
		Assertions.assertNotNull(kycResponse, "Expected non-null KYC response")
		Assertions.assertEquals(defaultUserId, kycResponse?.id, "User ID in KYC response doesn't match expected user ID")
	}



	@Test
	fun `default user can close an account`() {
		val request = HttpEntity<String>(headers)

		Assertions.assertNotNull(account1Number, "Account 1 number is not initialized")

		val response = restTemplate.exchange(
			baseUrl("/accounts/v1/accounts/{accountNumber}/close"),
			HttpMethod.POST,
			request,
			Void::class.java,
			account1Number
		)

		Assertions.assertEquals(HttpStatus.OK, response.statusCode)
	}



	@Test
	fun `default user can transfer money to another account`() {
		val transfer = mapOf(
			"sourceAccountNumber" to account2Number,
			"destinationAccountNumber" to account1Number,
			"amount" to BigDecimal(100)
		)

		val request = HttpEntity(transfer, headers)

		val response = restTemplate.exchange(
			baseUrl("/accounts/v1/accounts/transfer"),
			HttpMethod.POST,
			request,
			Void::class.java
		)

		Assertions.assertEquals(HttpStatus.OK, response.statusCode)
	}
}



