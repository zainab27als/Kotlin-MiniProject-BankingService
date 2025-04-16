package com.nbk.coded.bankingService.account

import com.nbk.coded.bankingService.user.UserRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*

@Service
class AccountService(val accountRepo: AccountRepository, val userRepo: UserRepository) {
    fun createAccount(userId: Long, name: String, initialBalance: BigDecimal, accountType: AccountType): Account {
        val user = userRepo.findById(userId).orElseThrow()
        val account = Account(user = user, accountNumber = UUID.randomUUID().toString(), balance = initialBalance, accountType = accountType)
        return accountRepo.save(account)
    }

    fun closeAccount(accountNumber: String) {
        val account = accountRepo.findAll().find { it.accountNumber == accountNumber }?.copy(isActive = false)
        if (account != null) {
            accountRepo.save(account)
        } else {
            throw NoSuchElementException("Account not found with number: $accountNumber")
        }
    }

    fun getAccountById(accountId: Long): Account {
        return accountRepo.findById(accountId).orElseThrow { throw NoSuchElementException("Account not found with ID: $accountId") }
    }

    fun getAccountByUserId(userId: Long): List<Account> {
        return accountRepo.findAllByUserId(userId)
    }
}
