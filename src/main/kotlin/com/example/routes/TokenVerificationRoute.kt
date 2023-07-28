package com.example.routes

import com.example.domain.model.*
import com.example.domain.repository.UserRepository
import com.example.security.TokenConfig
import com.example.util.Constants.AUDIENCE
import com.example.util.Constants.ISSUER
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun Route.tokenVerificationRoute(application: Application,userRepository: UserRepository,config:TokenConfig) {
    post(EndPoint.TokenVerification.path) {
        val request = call.receive<GoogleIdRequest>()
        if (request.tokenId.isNotEmpty()) {
            val result = verifyGoogleTokenId(tokenId = request.tokenId)
            if (result != null) {
                saveUserToDB(application = application,result = result,userRepository = userRepository)
            } else {
                application.log.info("Token verification failed")
                call.respondRedirect(EndPoint.Unauthorized.path)
            }
        } else {
            application.log.info("Empty token id")
            call.respondRedirect(EndPoint.Unauthorized.path)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.saveUserToDB(
    application: Application,
    result: GoogleIdToken,
    userRepository: UserRepository
) {
    application.log.info("Token verified")
    val sub = result.payload["sub"].toString()//unique id for google
    val name = result.payload["name"].toString()
    val email = result.payload["email"].toString()
    val profilePhoto = result.payload["photo"].toString()

    val user = User(
        //userId = sub,
        name = name,
        email = email,
        salt = name.length.toString()
    )
    val response = userRepository.saveUserInfo(user = user)

    if(response) {
        application.log.info("Token verified/user saved: $name,$email")
        call.sessions.set(UserSession(id = sub))
        call.respondRedirect(EndPoint.Authorized.path)
    }else{
        call.respondRedirect(EndPoint.Unauthorized.path)
    }
}

fun verifyGoogleTokenId(tokenId: String): GoogleIdToken? {
    return try {
        val verify = GoogleIdTokenVerifier.Builder(
            NetHttpTransport(),
            GsonFactory()
        ).setAudience(listOf(AUDIENCE))
            .setIssuer(ISSUER)
            .build()
        verify.verify(tokenId)
    } catch (e: Exception) {
        null
    }
}