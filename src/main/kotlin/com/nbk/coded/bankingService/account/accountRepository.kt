package com.nbk.coded.bankingService.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findAllByUserId(userId: Long): List<Account>
}