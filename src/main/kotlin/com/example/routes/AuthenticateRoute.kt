package com.example.routes

import com.example.domain.model.EndPoint
import com.example.routes.authenticate
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authenticate(){
    authenticate {
        get(EndPoint.Authenticate.path){
           // call.respond(HttpStatusCode.OK)
        }
    }
}