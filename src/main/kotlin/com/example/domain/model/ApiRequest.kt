package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiRequest<T>(var field:T?)

@Serializable
data class ParameterRequest<T,R>(var field:T?,var second:R?)
