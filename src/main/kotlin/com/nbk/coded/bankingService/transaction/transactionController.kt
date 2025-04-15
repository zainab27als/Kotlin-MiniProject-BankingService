package com.nbk.coded.bankingService.transaction

import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RestController
@RequestMapping("/transactions")
class TransactionController(val service: TransactionService) {

    data class TransferDTO(val sourceId: Long, val destId: Long, val amount: BigDecimal)

    @PostMapping("/transfer")
    fun transfer(@RequestBody dto: TransferDTO) {
        service.transfer(dto.sourceId, dto.destId, dto.amount)
    }
}

