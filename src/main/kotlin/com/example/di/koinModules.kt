package com.example.di

import com.example.data.ArchiveRepoImpl
import com.example.data.GeoFenceRepoImpl
import com.example.data.RelativeRepoImpl
import com.example.data.UserRepoImpl
import com.example.domain.repository.ArchiveRepository
import com.example.domain.repository.GeoFenceRepository
import com.example.domain.repository.RelativeRepository
import com.example.domain.repository.UserRepository
import com.example.util.Constants.DATABASE
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val kotlinModules = module{
    single {
        KMongo
        .createClient()
        .coroutine
        .getDatabase(DATABASE)
    }
    single<UserRepository> {
        UserRepoImpl(get())
    }
    single<GeoFenceRepository>{
        GeoFenceRepoImpl(get())
    }
    single<RelativeRepository> {
        RelativeRepoImpl(get())
    }
    single<ArchiveRepository> {
        ArchiveRepoImpl(get())
    }
}