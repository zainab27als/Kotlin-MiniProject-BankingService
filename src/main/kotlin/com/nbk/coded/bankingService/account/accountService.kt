package com.nbk.coded.bankingService.account

import com.nbk.coded.bankingService.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccountService(val accountRepo: AccountRepository, val userRepo: UserRepository) {
    fun createAccount(userId: Long): Account {
        val user = userRepo.findById(userId).orElseThrow()
        val account = Account(user = user, accountNumber = UUID.randomUUID().toString())
        return accountRepo.save(account)
    }

    fun closeAccount(accountId: Long) {
        val account = accountRepo.findById(accountId).orElseThrow()
        accountRepo.save(account.copy(isActive = false))
    }
    fun getAccountById(accountId: Long): Account {
        return accountRepo.findById(accountId).orElseThrow { throw NoSuchElementException("Account not found with ID: $accountId") }
    }

    fun getAccountByUserId(userId: Long): List<Account> {
        return accountRepo.findAllByUserId(userId)
    }
}