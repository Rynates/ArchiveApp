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
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.core.*
import java.io.File


const val INVENTORIES = "inventories"

@Location(INVENTORIES)
class Inventories

fun Route.addInventory(
    archiveRepository: ArchiveRepository,
    userRepository: UserRepository
) {
    post(INVENTORIES) {
        val userId = call.sessions.get<UserSession>()?.let { it -> userRepository.getUserByName(it.id) }
        val params = call.receiveParameters()

        val action = params["action"] ?: throw IllegalArgumentException("Missing action")

        if (userId == null) {
            call.redirect(LogIn())
        }

        when (action) {
            "delete" -> {
                val id = params["id"] ?: throw java.lang.IllegalArgumentException("Missing id")
                val fundId = params["fundId"] ?: throw java.lang.IllegalArgumentException("Missing id")
                archiveRepository.deleteInventoryById(id)

                val funds = archiveRepository.getInventoriesByFundId(fundId)
                val newFundList = mutableListOf<Document>()
                newFundList.addAll(funds!!)
                archiveRepository.addInventoryToFund(fundId, newFundList)
                call.respondRedirect("inventories/${fundId}")//ad Id Parameter

            }

            "add" -> {
                val id = params["fundId"] ?: throw java.lang.IllegalArgumentException("Missing id")
                archiveRepository.addInventory(createInventory(params))

                val funds = archiveRepository.getInventoriesByFundId(id)
                val newFundList = mutableListOf<Document>()
                if (funds != null) {
                    newFundList.addAll(funds)
                }
                archiveRepository.addInventoryToFund(id, newFundList)
                call.respondRedirect("inventories/${id}")//ad Id Parameter

            }

            "document" -> {
                val id = params["fundId"] ?: throw java.lang.IllegalArgumentException("Missing id")
//
//                val multipart = call.receiveMultipart()
//                val randomId = Random.nextInt()
//                multipart.forEachPart { part ->
//                    if (part is PartData.FileItem) {
//                        val targetDir = File("uploads" + File.separator + randomId)
//                        targetDir.mkdir()
//                        val targetFile = File(targetDir.path + File.separator + part.originalFileName)
//                        targetFile.createNewFile()
//                        part.streamProvider().use { input ->
//                            targetFile.outputStream().buffered().use { output -> input.copyTo(output) }
//                        }
//                        part.dispose()
//                    }
//
//                    call.respondRedirect("inventories/${id}")//ad Id Parameter
//
//                }
//                val multipart = call.receiveMultipart()
//                var title = ""
//                var videoFile: File? = null
//
//                multipart.forEachPart { part ->
//                    when (part) {
//                        is PartData.FormItem -> if (part.name == "title") {
//                            title = part.value
//                        }
//                        is PartData.FileItem -> {
//                            val ext = File(part.originalFileName).extension
//                            val file = File("uploads", "upload-${System.currentTimeMillis()}-${"AVX".hashCode()}-${title.hashCode()}.$ext")
//                            part.streamProvider().use { its -> file.outputStream().buffered().use {it-> its.copyTo(it)
//                            println(it)
//                            } }
//                            videoFile = file
//                        }
//
//                        is PartData.BinaryItem -> TODO()
//                        is PartData.BinaryChannelItem -> TODO()
//                    }
//
//                    part.dispose()
//                }
                val multipart = call.receiveMultipart()
                multipart.forEachPart { part ->
                    // if part is a file (could be form item)
                    if (part is PartData.FileItem) {
                        // retrieve file name of upload
                        val name = part.originalFileName!!
                        val file = File("/uploads/$name")

                        // use InputStream from part to save file
                        part.streamProvider().use { its ->
                            // copy the stream to the file with buffering
                            file.outputStream().buffered().use {
                                // note that this is blocking
                                its.copyTo(it)
                            }
                        }
                    }
                    part.dispose
                }
                // call.respondRedirect("inventories/${id}")//ad Id Parameter
            }
        }
    }
}


private fun createInventory(params: Parameters): Document {
    val name = params["name"] ?: throw IllegalArgumentException("Missing argument")
    val number = params["number"] ?: throw IllegalArgumentException("Missing argument")
    val fundId = params["fundId"] ?: throw IllegalArgumentException("Missing argument")

    return Document(
        name = name,
        number = number.toInt(),
        fundId = fundId
    )
}

fun Route.getInventories(
    archiveRepository: ArchiveRepository,
    userRepository: UserRepository
) {
    //api
    authenticate {
        post(EndPoint.GetInventoriesInfo.path) {
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val requestId = call.receiveOrNull<ApiRequest<String>>()
            requestId?.field?.let { getInventories(archiveRepository = archiveRepository, it) }
        }
    }
    //api
    authenticate {
        get(EndPoint.GetInventoriesInfo.path) {

            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val response = archiveRepository.getAllInventories()

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

    get("inventories/{id}") {
        val user = call.sessions.get<UserSession>()?.let { userRepository.getUserByName(it.id) }

        if (user == null) {
            call.redirect(LogIn())
        } else {
            val inventories = archiveRepository.getInventoriesByFundId("646a521b15f26370b362e630")
            val date = System.currentTimeMillis()
            val id = call.parameters.getOrFail<String>("id")

            call.respond(
                FreeMarkerContent(
                    template = "inventory.ftl",
                    model = mapOf(
                        "fundId" to id,
                        "inventories" to archiveRepository.getInventoriesByFundId(id),
                        "user" to user,
                        "date" to date
                    )
                )
            )

            call.respond(
                FreeMarkerContent(
                    "inventory.ftl",
                    mapOf("inventories" to inventories, "user" to user, "date" to date, "code" to "123")
                )
            )
        }
    }
}


private suspend fun PipelineContext<Unit, ApplicationCall>.getInventories(
    archiveRepository: ArchiveRepository,
    fundId: String
) {

    val response = archiveRepository.getInventoriesByFundId(fundId)

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

fun Route.getDocumentById(
    archiveRepository: ArchiveRepository
) {
    //api
    authenticate {
        post(EndPoint.GetInventoryById.path) {
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val requestId = call.receiveOrNull<ApiRequest<String>>()
            requestId?.field?.let { getDocumentById(archiveRepository = archiveRepository, it) }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getDocumentById(
    archiveRepository: ArchiveRepository,
    docId: String
) {

    val response = archiveRepository.getInventoryById(docId)

    if (response != null) {
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