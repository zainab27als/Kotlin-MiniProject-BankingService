package com.nbk.coded.bankingService.transaction

import com.nbk.coded.bankingService.account.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(
    val accountRepo: AccountRepository,
    val txRepo: TransactionRepository
) {

    fun transfer(sourceId: Long, destId: Long, amount: BigDecimal) {
        try {
            val source = accountRepo.findById(sourceId).orElseThrow {
                throw IllegalArgumentException("Source account not found with ID: $sourceId")
            }
            val dest = accountRepo.findById(destId).orElseThrow {
                throw IllegalArgumentException("Destination account not found with ID: $destId")
            }

            require(source.balance >= amount) { "Insufficient balance in source account" }

            val updatedSource = source.copy(balance = source.balance - amount)
            val updatedDest = dest.copy(balance = dest.balance + amount)

            accountRepo.saveAll(listOf(updatedSource, updatedDest))

            val tx = Transaction(sourceAccount = updatedSource, destinationAccount = updatedDest, amount = amount)
            txRepo.save(tx)

        } catch (e: Exception) {
            println("Error during transaction: ${e.message}")
            throw e
        }
    }

    fun getAllTransactions(): List<Transaction> {
        return txRepo.findAll()
    }
}
