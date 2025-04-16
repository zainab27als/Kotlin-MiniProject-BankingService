package com.nbk.coded.bankingService.account

import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/accounts/v1")
class AccountController(val service: AccountService) {

    data class CreateAccountDTO(val userId: Long, val name: String, val initialBalance: BigDecimal,
                                val accountType: AccountType)
    data class CloseAccountDTO(val accountId: Long)

    @PostMapping("/accounts")
    fun createAccount(@RequestBody dto: CreateAccountDTO): Account {
        return service.createAccount(dto.userId, dto.name, dto.initialBalance, dto.accountType)
    }

    @PostMapping("/accounts/{accountNumber}/close")
    fun closeAccount(@PathVariable accountNumber: String) {
        service.closeAccount(accountNumber)
    }

    @GetMapping("/accounts/{accountId}")
    fun getAccountById(@PathVariable accountId: Long): Account {
        return service.getAccountById(accountId)
    }

    @GetMapping("/accounts/user/{userId}")
    fun getAccountByUserId(@PathVariable userId: Long): List<Account> {
        return service.getAccountByUserId(userId)
    }
}

