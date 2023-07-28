package com.example.plugins

import com.example.domain.model.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.application.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer

import org.litote.kmongo.id.serialization.IdKotlinXSerializationModule
import java.util.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(json = Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            serializersModule = SerializersModule {
                contextual(ApiResponse.serializer(Relative.serializer()))
            }
            serializersModule = SerializersModule {
                contextual(ApiResponse.serializer(User.serializer()))
                contextual(ApiRequest.serializer(Relative.serializer()))
            }
            serializersModule = IdKotlinXSerializationModule

        }
        )
    }
}
