package com.example.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
data class ApiResponse<T>(
    val success:Boolean,
    val info:T?=null,
    val message:String
)

