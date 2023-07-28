package com.example.domain.repository

import com.example.domain.model.Relative

interface RelativeRepository {
    suspend fun addRelative(relative: Relative):Boolean
    suspend fun getRelativesByUserId(userId:String):List<Relative>?
    suspend fun getRelativeById(relativeId:String):Relative?
    suspend fun deleteRelativeById(relativeId: String):Boolean
    suspend fun updateRelativeById(relative: Relative): Boolean
    suspend fun getAllRelatives():List<Relative>?
}