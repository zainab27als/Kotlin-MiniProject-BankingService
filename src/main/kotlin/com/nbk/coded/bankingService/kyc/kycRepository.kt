package com.nbk.coded.bankingService.kyc

import org.springframework.data.jpa.repository.JpaRepository

interface KycRepository : JpaRepository<KYC, Long>