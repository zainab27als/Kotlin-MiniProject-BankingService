package com.nbk.coded.bankingService.kyc

import com.nbk.coded.bankingService.user.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class KycService(val kycRepo: KycRepository, val userRepo: UserRepository) {
    fun createOrUpdateKyc(userId: Long, dob: LocalDate, nationality: String, salary: BigDecimal): KYC {
        val kyc = KYC(id = userId, dateOfBirth = dob, nationality = nationality, salary = salary)
        return kycRepo.save(kyc)
    }

    fun getKyc(userId: Long): KYC = kycRepo.findById(userId).orElseThrow()

    fun getAllKyc(): List<KYC> = kycRepo.findAll()
}