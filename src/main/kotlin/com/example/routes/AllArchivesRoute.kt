package com.example.routes

import com.example.domain.model.ApiResponse
import com.example.domain.model.EndPoint
import com.example.domain.model.UserSession
import com.example.domain.repository.ArchiveRepository
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.freemarker.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

const val ARCHIVES = "archives"

@Location(ARCHIVES)
class Archives

fun Route.getAllArchives(
    archiveRepository: ArchiveRepository,
    userRepository:UserRepository
) {
    //api
    authenticate {
        get(EndPoint.GetArchivesInfo.path) {
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            getArchives(archiveRepository = archiveRepository)
        }
    }

    get(ARCHIVES) {
        val user = call.sessions.get<UserSession>()?.let { userRepository.getUserByName(it.id) }

        if (user == null) {
            call.redirect(LogIn())
        } else {
            val archives = archiveRepository.getAllArchives()
            val date = System.currentTimeMillis()

            call.respond(
                FreeMarkerContent(
                    template = "archives.ftl",
                    model = mapOf(
                        "archives" to archiveRepository.getAllArchives(),
                        "user" to user,
                        "date" to date
                    )
                )
            )

            call.respond(
                FreeMarkerContent(
                    "archives.ftl",
                    mapOf("archives" to archives, "user" to user, "date" to date, "code" to "123")
                )
            )
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getArchives(
    archiveRepository: ArchiveRepository
) {

    val response = archiveRepository.getAllArchives()

    if (response!!.isNotEmpty()) {
        call.respond(
            message = ApiResponse(
                success = true,
                info = response,
                message = "Successfully get"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        call.respond(
            message = ApiResponse(
                success = false,
                info = response,
                message = "Unsuccessfully get"
            ),
            status = HttpStatusCode.BadRequest
        )
    }
}