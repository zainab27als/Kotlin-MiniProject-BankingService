package com.nbk.coded.bankingService.account

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/accounts")
class AccountController(val service: AccountService) {

    data class CreateAccountDTO(val userId: Long)
    data class CloseAccountDTO(val accountId: Long)

    @PostMapping("/create")
    fun createAccount(@RequestBody dto: CreateAccountDTO): Account {
        return service.createAccount(dto.userId)
    }

    @PostMapping("/close")
    fun closeAccount(@RequestBody dto: CloseAccountDTO) {
        service.closeAccount(dto.accountId)
    }
    @GetMapping("/{accountId}")
    fun getAccountById(@PathVariable accountId: Long): Account {
        return service.getAccountById(accountId)
    }

    @GetMapping("/user/{userId}")
    fun getAccountByUserId(@PathVariable userId: Long): List<Account> {
        return service.getAccountByUserId(userId)
    }
}
