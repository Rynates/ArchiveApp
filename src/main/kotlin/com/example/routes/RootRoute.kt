package com.example.routes

import com.example.domain.model.EndPoint
import com.example.domain.model.User
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val HOME = "/"

@Location(HOME)
class Home
fun Routing.rootRoute(){
    get(HOME) {
        call.respond(FreeMarkerContent("home.ftl", User("13")))
    }
    get(EndPoint.About.path){
        call.respond(FreeMarkerContent("about.ftl", User("13")))
    }
}