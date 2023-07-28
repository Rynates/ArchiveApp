package com.example.security

import io.ktor.server.auth.*


data class TokenClaim(
    val name: String,
    val value: String
): Principal