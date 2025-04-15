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
}
