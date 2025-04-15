package com.nbk.coded.bankingService.account

import com.nbk.coded.bankingService.user.User
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "account")
data class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,

    val balance: BigDecimal = BigDecimal.ZERO,
    val isActive: Boolean = true,
    val accountNumber: String = UUID.randomUUID().toString()
){
    constructor() : this(
        id = null,
        user = null,
        balance = BigDecimal.ZERO,
        isActive = true,
        accountNumber = UUID.randomUUID().toString()
    )
}