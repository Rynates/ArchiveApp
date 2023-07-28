package com.example

import io.ktor.server.application.*
import com.example.plugins.*
import com.example.security.JwtTokenService
import com.example.security.SHA256HashingService
import com.example.security.TokenConfig

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused")
fun Application.module() {

    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()

    freeMarker()
    configureKoin()
    configureLocations()
    configureSession()
    configureSecurity(tokenConfig)
    configureRouting(hashingService,tokenService,tokenConfig)
    configureSerialization()
    configureMonitoring()


}
