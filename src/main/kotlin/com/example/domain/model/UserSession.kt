package com.example.domain.model

import io.ktor.server.auth.*
import java.security.Principal


data class UserSession(
    val id:String
): Principal
