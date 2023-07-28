package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username:String?=null,
    val email: String,
    val password: String
)
