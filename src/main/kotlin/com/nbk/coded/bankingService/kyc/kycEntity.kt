package com.nbk.coded.bankingService.kyc

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "kyc")
data class KYC(
    @Id
    val id: Long? = null,

    val dateOfBirth: LocalDate? = null,
    val nationality: String? = null,
    val salary: BigDecimal? = null
){
    constructor() : this(id = null, dateOfBirth = null, nationality = null, salary = null)
}