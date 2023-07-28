package com.example.plugins

import com.example.di.kotlinModules
import io.ktor.server.application.*
import io.ktor.server.plugins.doublereceive.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin(){
    install(Koin){
        modules(kotlinModules)
    }
    install(DoubleReceive)
}