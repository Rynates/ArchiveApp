package com.example.routes

import com.example.domain.model.*
import com.example.domain.repository.ArchiveRepository
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.pipeline.*

fun Route.addArchive(
    archiveRepository: ArchiveRepository,
    userRepository:UserRepository
) {
    //api
    post(EndPoint.AddArchiveInfo.path) {
        val request = call.receiveOrNull<ApiRequest<Archive>>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        addArchive(request.field, archiveRepository)
    }

    post(ARCHIVES) {
        val user = call.sessions.get<UserSession>()?.let { it -> userRepository.getUserByName(it.id)}
        val params = call.receiveParameters()
        val action = params["action"] ?: throw IllegalArgumentException("Missing action")

        if (user == null) {
            call.redirect(LogIn())
        }

        when (action) {
            "delete" -> {
                val id = params["id"] ?: throw java.lang.IllegalArgumentException("Missing id")
                archiveRepository.deleteArchiveById(id)
            }
            "add" -> {
                archiveRepository.addArchive(createArchive(params))
            }
            "funds" ->{
                val id = params["archiveId"] ?: throw java.lang.IllegalArgumentException("Missing id")
                call.respondRedirect("funds/${id}")
            }
        }

         call.redirect(Archives())
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.addArchive(
    request: Archive?,
    archiveRepository: ArchiveRepository
) {
    if (request != null) {
        val archive = Archive(
            name = request.name
        )

        val response = archiveRepository.addArchive(archive = archive)

        if (response) {
            call.respond(
                message = ApiResponse<Archive>(
                    success = true,
                    info = request,
                    message = "Successfully added"
                ),
                status = HttpStatusCode.OK
            )
        } else {
            call.respond(
                message = ApiResponse<Archive>(
                    success = false,
                    info = request,
                    message = "Unsuccessfully added"
                ),
                status = HttpStatusCode.BadRequest
            )
        }
    }
}

fun Route.getArchiveById(
    archiveRepository: ArchiveRepository
) {
    post(EndPoint.GetArchiveById.path) {
        val request = call.receiveOrNull<ApiRequest<String>>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        request.field?.let { it1 -> getArchiveById(it1, archiveRepository) }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getArchiveById(
    request: String,
    archiveRepository: ArchiveRepository
) {

        val response = archiveRepository.getArchiveById(archiveId = request)

    if(response!=null){
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

private fun createArchive(params: Parameters): Archive {
    val name = params["name"] ?: throw IllegalArgumentException("Missing argument: destination")
    val city = params["city"] ?: throw IllegalArgumentException("Missing argument: destination")
    val type = params["type"] ?: throw IllegalArgumentException("Missing argument: arrivalDate")
    val phone = params["phone"] ?: throw IllegalArgumentException("Missing argument: airline")
    val address = params["address"] ?: throw IllegalArgumentException("Missing argument: people")
    val email = params["email"] ?: throw IllegalArgumentException("Missing argument: price")

    return Archive(
        name = name,
        city = city,
        type = type,
        phone = phone,
        address = address,
        email = email
    )
}