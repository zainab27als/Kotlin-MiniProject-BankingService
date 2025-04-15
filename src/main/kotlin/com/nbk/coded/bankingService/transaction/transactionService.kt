package com.nbk.coded.bankingService.transaction

import com.nbk.coded.bankingService.account.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(val accountRepo: AccountRepository, val txRepo: TransactionRepository) {
    fun transfer(sourceId: Long, destId: Long, amount: BigDecimal) {
        val source = accountRepo.findById(sourceId).orElseThrow()
        val dest = accountRepo.findById(destId).orElseThrow()

        require(source.balance >= amount)

        val updatedSource = source.copy(balance = source.balance - amount)
        val updatedDest = dest.copy(balance = dest.balance + amount)

        accountRepo.saveAll(listOf(updatedSource, updatedDest))

        val tx = Transaction(sourceAccount = updatedSource, destinationAccount = updatedDest, amount = amount)
        txRepo.save(tx)
    }
}