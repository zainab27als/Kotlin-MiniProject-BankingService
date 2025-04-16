package com.nbk.coded.bankingService.transaction

import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/accounts/v1/accounts/transfer")
class TransactionController(val service: TransactionService) {

    data class TransferDTO(val sourceAccountNumber: String, val destinationAccountNumber: String, val amount: BigDecimal)

    @PostMapping
    fun transfer(@RequestBody dto: TransferDTO) {
        service.transfer(dto.sourceAccountNumber, dto.destinationAccountNumber, dto.amount)
    }

    @GetMapping
    fun getAllTransactions(): List<Transaction> {
        return service.getAllTransactions()
    }
}


