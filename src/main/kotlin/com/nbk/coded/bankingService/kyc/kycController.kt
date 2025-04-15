package com.nbk.coded.bankingService.kyc

import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate

@RestController
@RequestMapping("/kyc")
class KycController(val service: KycService) {

    data class KycDTO(val userId: Long, val dob: String, val nationality: String, val salary: BigDecimal)

    @PostMapping("/update")
    fun updateKyc(@RequestBody dto: KycDTO): KYC {
        val date = LocalDate.parse(dto.dob)
        return service.createOrUpdateKyc(dto.userId, date, dto.nationality, dto.salary)
    }

    @GetMapping
    fun getKyc(@RequestParam userId: Long): KYC = service.getKyc(userId)
}
