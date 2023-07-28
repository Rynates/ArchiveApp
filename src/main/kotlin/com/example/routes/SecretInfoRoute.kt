package com.example.routes

import com.example.domain.model.EndPoint
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Route.getSecretInfo(){
    authenticate{
        get(EndPoint.GetSecretInfo.path){
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("id",String::class)
            call.respond(HttpStatusCode.OK,"Your userId is $userId")
        }
    }
}