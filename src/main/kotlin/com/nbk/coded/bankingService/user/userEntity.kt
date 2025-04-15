package com.nbk.coded.bankingService.user

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var username: String? = null,
    @Column(nullable = false)
    var password: String? = null
)
