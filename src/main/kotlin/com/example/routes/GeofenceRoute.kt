package com.example.routes

import com.example.domain.model.*
import com.example.domain.repository.GeoFenceRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.addGeoFence(
    geoFenceRepository: GeoFenceRepository
) {
    authenticate {
        post(EndPoint.AddGeoFenceInfo.path) {

            val request = call.receiveOrNull<ApiRequest<GeoFence>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            addGeoFence(request= request.field!!,geoFenceRepository = geoFenceRepository)
        }
    }
}
private suspend fun PipelineContext<Unit, ApplicationCall>.addGeoFence(
    request: GeoFence,
    geoFenceRepository: GeoFenceRepository
) {

    val geofence = GeoFence(
        geoId = request.geoId,
        name = request.name,
        latitude = request.latitude,
        longitude = request.longitude,
        location = request.location,
        radius = request.radius,
        time = request.time,
        year = request.year,
        createdBy = request.createdBy
    )

    val response = geoFenceRepository.addGeoFence(
        geoFence = geofence
    )
    if(response){
        call.respond(
            message = ApiResponse<GeoFence>(
                success = true,
                message = "Successfully added"
            ),
            status = HttpStatusCode.OK
        )
    }else{
        call.respond(
            message = ApiResponse<GeoFence>(success = false, message = "Unsuccessfully added"),
            status = HttpStatusCode.BadRequest
        )
    }
}
fun Route.getAllGeoFences(
    geoFenceRepository: GeoFenceRepository
){
    authenticate {
        get(EndPoint.GetAllGeoFencesInfo.path){
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")
            getGeoFences(geoFenceRepository = geoFenceRepository)

        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getGeoFences(
    geoFenceRepository: GeoFenceRepository
) {

    val response = geoFenceRepository.getAllGeoFences()

        call.respond(
            message = ApiResponse(
                success = true,
                info = response,
                message = "Successfully get"
            ),
            status = HttpStatusCode.OK
        )

}


fun Route.getGeofence(
    geoFenceRepository: GeoFenceRepository
){
    authenticate {
        post(EndPoint.GetGeoFenceInfo.path){
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")

            val request = call.receiveOrNull<ApiRequest<String>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            getGeoFence(userId = request.field!!,geoFenceRepository = geoFenceRepository)
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getGeoFence(
    userId:String,
    geoFenceRepository: GeoFenceRepository
) {

    val response = geoFenceRepository.getGeoFencesByUserId(userId = userId)

    if(response.isNotEmpty()){
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
fun Route.deleteGeofence(
    geoFenceRepository: GeoFenceRepository
) {
    authenticate {
        post(EndPoint.DeleteGeoFenceInfo.path) {
            val request = call.receiveOrNull<ApiRequest<Int>>()
            getGeofenceInfo(geofenceId = request?.field!!, geoFenceRepository = geoFenceRepository) }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getGeofenceInfo(
    geofenceId:Int,
    geoFenceRepository: GeoFenceRepository
) {

    val response = geoFenceRepository.deleteGeoFenceByID(geoId = geofenceId)

    if (response) {
        call.respond(
            message = ApiResponse<Boolean>(
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