package com.example.routes

import com.example.domain.model.AuthRequest
import com.example.domain.model.AuthResponse
import com.example.domain.model.EndPoint
import com.example.domain.model.UserSession
import com.example.domain.repository.UserRepository
import com.example.security.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.locations.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.apache.commons.codec.digest.DigestUtils
import org.litote.kmongo.util.idValue

const val SIGN_IN = "log_in"

@OptIn(KtorExperimentalLocationsAPI::class)
@Location(SIGN_IN)
data class LogIn(val userId: String = "", val error: String = "")


fun Route.signIn(
    userRepository: UserRepository,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
){
    post(SIGN_IN){
        val signInParameters = call.receive<Parameters>()
        val userId = signInParameters["userId"] ?: return@post call.redirect(it)
        val password = signInParameters["password"] ?: return@post call.redirect(it)

        val signInError = LogIn(userId)

        val signin =  userRepository.getUserByName(userId)

        if (signin == null) {
            call.redirect(signInError.copy(error = "Invalid username or password"))
        }
        if(signin?.name == "admin"){
            call.sessions.set(UserSession(signin.name))
            call.redirect(Archives())
        } else{
            call.redirect(signInError.copy(error = "Invalid username or password"))
        }
    }
    get(SIGN_IN) {
        val user = call.sessions.get<UserSession>()?.let { it -> userRepository.getUserByName(it.id) }

        if (user != null) {
            call.redirect(Archives())
        } else {
            call.respond(FreeMarkerContent("signin.ftl", mapOf("userId" to it.idValue, "error" to "error"), ""))
        }
    }

    //api
    post(EndPoint.SignIn.path) {
        val request = call.receiveOrNull<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val user = userRepository.getUserByEmail(request.email)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect email or password")
            return@post
        }
        val isValidPassword = hashingService.verify(
            value = request.password,
            saltedHash = SaltedHash(
                hash = user.password!!,
                salt = user.salt!!
            )
        )
        if (!isValidPassword) {
            println("Entered hash: ${DigestUtils.sha256Hex("${user.salt}${request.password}")}, Hashed PW: ${user.password},Salt: ${user.salt},${isValidPassword}")
            call.respond(HttpStatusCode.Conflict, "Incorrect password")
            return@post
        } else {
            val token = tokenService.generate(
                config = tokenConfig,
                TokenClaim(
                    name = "id",
                    value = user.id.toString()
                )
            )

            call.respond(
                status = HttpStatusCode.OK,
                message = AuthResponse(
                    token = token
                )
            )
        }
    }
}
@OptIn(KtorExperimentalLocationsAPI::class)
suspend fun ApplicationCall.redirect(location: Any) {
    respondRedirect(application.locations.href(location))
}