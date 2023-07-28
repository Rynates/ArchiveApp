package com.example.routes

import com.example.domain.model.*
import com.example.domain.repository.ArchiveRepository
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.freemarker.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*


const val FUNDS = "funds"

@Location(FUNDS)
class Funds

fun Route.addFund(
    archiveRepository: ArchiveRepository,
    userRepository: UserRepository
) {
    post(FUNDS) {
        val userId = call.sessions.get<UserSession>()?.let { it -> userRepository.getUserByName(it.id) }
        val params = call.receiveParameters()
        val action = params["action"] ?: throw IllegalArgumentException("Missing action")

        if (userId == null) {
            call.redirect(LogIn())
        }

        when (action) {
            "delete" -> {
                val id = params["id"] ?: throw java.lang.IllegalArgumentException("Missing id")
                val archiveId = params["archiveId"] ?: throw java.lang.IllegalArgumentException("Missing id")
                archiveRepository.deleteFundById(id)

                val funds = archiveRepository.getFundsByArchiveId(archiveId)
                val newFundList = mutableListOf<Fund>()
                newFundList.addAll(funds!!)
                archiveRepository.addFundToArchive(archiveId,newFundList)
                call.respondRedirect("funds/${archiveId}")
            }

            "add" -> {
                val id = params["archiveId"] ?: throw java.lang.IllegalArgumentException("Missing id")
                archiveRepository.addFund(createFund(params))

                val funds = archiveRepository.getFundsByArchiveId(id)
                val newFundList = mutableListOf<Fund>()
                if (funds != null) {
                    newFundList.addAll(funds)
                }
                archiveRepository.addFundToArchive(id,newFundList)
                call.respondRedirect("funds/${id}")//ad Id Parameter

            }
            "inventories" ->{
                val id = params["fundId"] ?: throw java.lang.IllegalArgumentException("Missing id")
                call.respondRedirect("inventories/${id}")
            }

        }

    }
}


//private inline fun <reified T> createArchive(params: Parameters): Any {
//    val name = params["name"] ?: throw IllegalArgumentException("Missing argument: destination")
//    val city = params["number"] ?: throw IllegalArgumentException("Missing argument: destination")
//
//    return when(T::class) {
//        Archive::class ->
//        Archive(
//            name = name,
//            city = city
//        )
//        Fund::class ->
//            Fund(
//                number = number,
//                name = name,
//
//            )
//        else -> {}
//    }
//}

private fun createFund(params: Parameters): Fund {
    val name = params["name"] ?: throw IllegalArgumentException("Missing argument")
    val number = params["number"] ?: throw IllegalArgumentException("Missing argument")
    val archiveId = params["archiveId"] ?: throw IllegalArgumentException("Missing argument")
    val inventory = params["inventor"] ?:throw IllegalArgumentException("Missing argument")
    val digitization = params["digitization"]?: throw IllegalArgumentException("Missing argument")

    return Fund(
        name = name,
        number = number.toInt(),
        archiveId = archiveId,
        inventoryNum = inventory,
        digitization = digitization
    )
}

fun Route.getFunds(
    archiveRepository: ArchiveRepository,
    userRepository: UserRepository
) {
    //api
    authenticate {
        post(EndPoint.GetFundsInfo.path) {
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val requestId = call.receiveOrNull<ApiRequest<String>>()
            requestId?.field?.let { getFunds(archiveRepository = archiveRepository, it) }
        }
    }
    //api
    authenticate {
        get(EndPoint.GetFundsInfo.path) {

            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val response = archiveRepository.getAllFunds()

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
    }
    //api
    authenticate {
        post(EndPoint.GetFundsByIdInfo.path) {

            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val requestId = call.receiveOrNull<ApiRequest<String>>()

            val response = archiveRepository.getFundById(requestId?.field!!)

            if (response!= null) {
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
    }

    get("funds/{id}") {
        val user = call.sessions.get<UserSession>()?.let { userRepository.getUserByName(it.id) }

        if (user == null) {
            call.redirect(LogIn())
        } else {
            val funds = archiveRepository.getFundsByArchiveId("645609fc141fb27244da1772")
            val date = System.currentTimeMillis()
            val id = call.parameters.getOrFail<String>("id")

            call.respond(
                FreeMarkerContent(
                    template = "funds.ftl",
                    model = mapOf(
                        "archiveId" to id,
                        "funds" to archiveRepository.getFundsByArchiveId(id),
                        "user" to user,
                        "date" to date
                    )
                )
            )

            call.respond(
                FreeMarkerContent(
                    "funds.ftl",
                    mapOf("funds" to funds, "user" to user, "date" to date, "code" to "123")
                )
            )
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getFundById(
    archiveRepository: ArchiveRepository,
    fundId: String
) {

    val response = archiveRepository.getFundById(fundId)

    if (response!= null) {
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

private suspend fun PipelineContext<Unit, ApplicationCall>.getFunds(
    archiveRepository: ArchiveRepository,
    archiveId: String
) {

    val response = archiveRepository.getFundsByArchiveId(archiveId = archiveId)

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