package com.example.routes

import com.example.domain.model.ApiRequest
import com.example.domain.model.ApiResponse
import com.example.domain.model.EndPoint
import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import org.litote.kmongo.toId

fun Route.getUserInfo(
    userRepository: UserRepository
) {
    authenticate {
        get(EndPoint.GetUserInfo.path) {
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)

            println("Token is: ${userId!!.toId<User>()}")

            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest)

            } else {

                    val user = userRepository.getUserById(userId = userId)

                println("${user!!.id}")

                    if (user == null) {
                        call.respond(
                            message = "Unsuccessful get",
                            status = HttpStatusCode.Conflict
                        )
                    } else {
                        call.respond(
                            message = ApiResponse(
                                message = "Success",
                                info = user,
                                success = true
                            ),
                            status = HttpStatusCode.OK
                        )
                    }

            }
        }

    }
}

fun Route.getUserById(
    userRepository: UserRepository
){
    post(EndPoint.GetOtherUserById.path){
        val request = call.receiveOrNull<ApiRequest<String>>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        getUserById(userId = request.field!!, userRepository = userRepository)
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.getUserById(
    userId:String,
    userRepository: UserRepository
) {

    val response = userRepository.getUserById(userId = userId)


    if(response== null){
        call.respond(
            message = ApiResponse<User>(
                success = false,
                info = response,
                message = "Unsuccessfully get"
            ),
            status = HttpStatusCode.BadRequest
        )
    }else{
        call.respond(
            message = ApiResponse<User>(
                success = true,
                info = response,
                message = "Successfully get"
            ),
            status = HttpStatusCode.OK
        )
    }
}

fun Route.getOtherUserByName(
    userRepository: UserRepository
){
    post(EndPoint.GetOtherUserByName.path){
        val request = call.receiveOrNull<ApiRequest<String>>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        getOtherUserByName(userName = request.field!!, userRepository = userRepository)
    }
}

fun Route.getAllUsers(
    userRepository: UserRepository
){
    get(EndPoint.GetAllUsers.path) {

        val userList = userRepository.getAllUsers()

        call.respond(
            message = ApiResponse(
                message = "Success",
                info = userList,
                success = true
            ),
            status = HttpStatusCode.OK
        )

    }
}
private suspend fun PipelineContext<Unit, ApplicationCall>.getOtherUserByName(
    userName:String,
    userRepository: UserRepository
) {

    val response = userRepository.getUserByName(userName = userName)


    if(response== null){
        call.respond(
            message = ApiResponse<User>(
                success = false,
                info = response,
                message = "Unsuccessfully get"
            ),
            status = HttpStatusCode.BadRequest
        )
    }else{
        call.respond(
            message = ApiResponse<User>(
                success = true,
                info = response,
                message = "Successfully get"
            ),
            status = HttpStatusCode.OK
        )
    }
}

fun Route.updateUserInfo(
    userRepository: UserRepository
) {
    authenticate {
        put(EndPoint.UpdateUserInfo.path) {
            val userSession = call.principal<JWTPrincipal>()
            val userId = userSession!!.getClaim("id", String::class)
            println("Token is: ${userId!!}")


            val user = userRepository.getUserById(userId = userId)

            val request = call.receiveOrNull<ApiRequest<User>>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@put
            }

            if (user != null) {
                updateUser(user = user.copy(name = request.field!!.name ?:user.name,photo = request.field!!.photo ?: user.photo,salt = user.salt, location = request.field?.location ?:user.location, info = request.field?.info?:user.info, isEmailOpen = request.field?.isEmailOpen?: user.isEmailOpen, listOfDocuments = request.field?.listOfDocuments?: user.listOfDocuments), userRepository)
            }
            else{
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}


private suspend fun PipelineContext<Unit, ApplicationCall>.updateUser(
    user: User,
    userRepository: UserRepository
) {
    val response = userRepository.updateUserInfoById(
        userId = user.id!!,
        user = user
    )
    if (response) {
        call.respond(
            message = ApiResponse<User>(
                success = true,
                info = user,
                message = "Successfully updated"
            ),
            status = HttpStatusCode.OK
        )
    } else {
        call.respond(
            message = ApiResponse<User>(success = false, message = "Unsuccessfully updated"),
            status = HttpStatusCode.BadRequest
        )
    }
}