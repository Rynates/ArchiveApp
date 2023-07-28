package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class GoogleIdRequest (
    val tokenId:String
)