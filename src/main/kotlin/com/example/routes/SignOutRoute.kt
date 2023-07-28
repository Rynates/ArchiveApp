package com.example.routes

import com.example.domain.model.UserSession
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

const val SIGN_OUT = "/signout"

@Location(SIGN_OUT)
class SignOut


fun Route.signOut() {
        get(SIGN_OUT) {
            call.sessions.clear<UserSession>()
            call.redirect(LogIn())
        }
}