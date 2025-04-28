package com.nbk.coded.bankingService.kyc

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate

@RestController
@RequestMapping("/users/v1/kyc")
class KycController(val service: KycService) {

    data class KycDTO(val userId: Long, val dob: String, val nationality: String, val salary: BigDecimal)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrUpdateKyc(@RequestBody dto: KycDTO): KYC {
        val date = LocalDate.parse(dto.dob)
        return service.createOrUpdateKyc(dto.userId, date, dto.nationality, dto.salary)
    }

    @GetMapping("/{userId}")
    fun getKyc(@PathVariable userId: Long): KYC = service.getKyc(userId)

    @GetMapping("/all")
    fun getAllKyc(): List<KYC> = service.getAllKyc()
}

