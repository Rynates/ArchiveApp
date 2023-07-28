package com.example.plugins

import com.example.domain.repository.ArchiveRepository
import com.example.domain.repository.GeoFenceRepository
import com.example.domain.repository.RelativeRepository
import com.example.domain.repository.UserRepository
import com.example.routes.*
import com.example.security.HashingService
import com.example.security.TokenConfig
import com.example.security.TokenService
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import org.koin.java.KoinJavaComponent.inject

fun Application.configureRouting(
    hashService:HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        static("/static") {
            resources("images")
            resources("uploads")
        }
        val userRepository: UserRepository by inject(UserRepository::class.java)
        val relativeRepository: RelativeRepository by inject(RelativeRepository::class.java)
        val archiveRepository:ArchiveRepository by inject(ArchiveRepository::class.java)
        val geoFenceRepository:GeoFenceRepository by inject(GeoFenceRepository::class.java)

        rootRoute()
        signUp(hashService,userRepository)
        signIn(userRepository,hashService,tokenService,tokenConfig)
        signOut()
        authenticate()
        getSecretInfo()
        addFund(archiveRepository,userRepository)
        getFunds(archiveRepository,userRepository)
        addInventory(archiveRepository,userRepository)
        getInventories(archiveRepository,userRepository)
        addRelativeRoute(relativeRepository)
        getRelative(relativeRepository)
        deleteRelative(relativeRepository)
        getUserInfo(userRepository)
        updateUserInfo(userRepository)
        getRelativeById(relativeRepository)
        addArchive(archiveRepository,userRepository)
        getAllArchives(archiveRepository,userRepository)
        addGeoFence(geoFenceRepository)
        getGeofence(geoFenceRepository)
        getAllGeoFences(geoFenceRepository)
        getUserById(userRepository)
        getOtherUserByName(userRepository)
        deleteGeofence(geoFenceRepository)
        updateRelative(relativeRepository)
        getAllRelatives(relativeRepository)
        getAllUsers(userRepository)
        getDocumentById(archiveRepository)
        getArchiveById(archiveRepository)
        tokenVerificationRoute(application,userRepository,tokenConfig)
        configureUpdateUserInfo(application,userRepository)
        unauthorized()
        authorizedRoute()
    }
}
