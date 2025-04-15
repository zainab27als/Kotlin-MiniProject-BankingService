package com.nbk.coded.bankingService.transaction

import com.nbk.coded.bankingService.account.Account
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "transaction")
data class Transaction(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    val sourceAccount: Account? = null,

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    val destinationAccount: Account? = null,

    val amount: BigDecimal = BigDecimal.ZERO
){

    constructor() : this(0, null, null, BigDecimal.ZERO)
}