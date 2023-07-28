package com.example.plugins

import com.example.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import java.io.File

fun Application.configureSession(){
    install(Sessions){
        val secretEncKey = hex("00112233454545454899aabbccddeeff")
        val secretAuthKey = hex("02030405454548090a0b0c")
        cookie<UserSession>(
            name = "USER_SESSION",
            storage = directorySessionStorage(File(".sessions"))
        ){
            transform(SessionTransportTransformerEncrypt(secretEncKey,secretAuthKey))
        }
    }
}