package com.example.routes

import com.example.domain.model.*
import com.example.domain.repository.RelativeRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*


fun Route.addRelativeRoute(
    relativeRepository: RelativeRepository
) {
    authenticate {
        post(EndPoint.AddRelativeInfo.path) {
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val request = call.receiveOrNull<ApiRequest<Relative>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            addRelativeInfo(userId, request.field!!, relativeRepository = relativeRepository)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.addRelativeInfo(
    userId: String,
    request: Relative,
    relativeRepository: RelativeRepository
) {
    val relative = Relative(
        gender = request.gender,
        livingStatus = request.livingStatus,
        birthDate = request.birthDate,
        name = request.name,
        surname = request.surname,
        secondName = request.secondName,
        birthPlace = request.birthPlace,
        userId = userId,
        placeOfLiving = request.placeOfLiving
    )

    val response = relativeRepository.addRelative(relative = relative)

    val listOfRelatives = relativeRepository.getRelativesByUserId(userId = userId)

    if (response) {
        call.respond(
            message = ApiResponse<List<Relative>>(
                success = true,
                info = listOfRelatives,
                message = "Successfully added"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        call.respond(
            message = ApiResponse<List<Relative>>(
                success = false,
                info = listOfRelatives,
                message = "Unsuccessfully added"
            ),
            status = HttpStatusCode.BadRequest
        )
    }
}
fun Route.deleteRelative(
    repository: RelativeRepository
) {
    authenticate {
        post(EndPoint.DeleteRelativeInfo.path) {
            val request = call.receiveOrNull<ApiRequest<String>>()
            deleteRelativeInfo(request!!.field!!, relativeRepository = repository)

        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.deleteRelativeInfo(
    relativeId: String,
    relativeRepository: RelativeRepository
) {

    val response = relativeRepository.deleteRelativeById(relativeId = relativeId)

    if (response) {
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
fun Route.getRelative(
    relativeRepository: RelativeRepository
){
    authenticate {
        get(EndPoint.GetRelativeInfo.path){
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            getRelativeInfo(userId,relativeRepository)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getRelativeInfo(
    userId:String,
    relativeRepository: RelativeRepository
) {

    val response = relativeRepository.getRelativesByUserId(userId = userId)

    if(!response.isNullOrEmpty()){
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
fun Route.getRelativeById(
    relativeRepository: RelativeRepository
){
    authenticate {
        post(EndPoint.GetRelativeInfoById.path){
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val request = call.receive<ApiRequest<String>>()
            request.field?.let { it1 -> getRelativeByIdInfo(it1,relativeRepository) }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getRelativeByIdInfo(
    relativeId:String,
    relativeRepository: RelativeRepository
) {


    val response = relativeRepository.getRelativeById(relativeId = relativeId)

    if(response !== null){
        call.respond(
            message = ApiResponse<Relative>(
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

fun Route.updateRelative(
    relativeRepository: RelativeRepository
){
    authenticate {
        put(EndPoint.UpdateRelativeInfo.path){
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val request = call.receive<ApiRequest<Relative>>()
            val getRelative = relativeRepository.getRelativeById(request.field?.id!!)

            request.field?.let { it1 -> updateRelativeInfo(relative = getRelative!!.copy(
                id = request.field?.id,
                name = request.field?.name?:getRelative.name,
                surname = request.field?.surname ?:getRelative.surname,
                secondName = request.field?.secondName ?:getRelative.secondName,
                birthPlace = request.field?.birthPlace ?: getRelative.birthPlace,
                birthDate = request.field?.birthDate ?: getRelative.birthDate,
                placeOfLiving = request.field?.placeOfLiving ?: getRelative.placeOfLiving,
                livingStatus = request.field?.livingStatus ?: getRelative.livingStatus,
                photo = request.field?.photo ?: getRelative.photo,
                fatherId = request.field?.fatherId ?: getRelative.fatherId,
                motherId = request.field?.motherId ?: getRelative.motherId,
                link = request.field?.link ?: getRelative.link,
                relativeDoc = request.field?.relativeDoc ?: getRelative.relativeDoc
                ),relativeRepository) }
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.updateRelativeInfo(
    relative:Relative,
    relativeRepository: RelativeRepository
) {


    val response = relativeRepository.updateRelativeById(relative)

    call.respond(
        message = ApiResponse(
            success = true,
            info = response,
            message = "Successfully get"
        ),
        status = HttpStatusCode.OK
    )
}
fun Route.getAllRelatives(
    relativeRepository: RelativeRepository
){
    authenticate {
        get(EndPoint.GetAllRelativesInfo.path){
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            getAllRelativesInfo(relativeRepository)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getAllRelativesInfo(
    relativeRepository: RelativeRepository
) {

    val response = relativeRepository.getAllRelatives()

    if(!response.isNullOrEmpty()){
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