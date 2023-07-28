package com.example.domain.repository

import com.example.domain.model.GeoFence

interface GeoFenceRepository {
    suspend fun addGeoFence(geoFence: GeoFence):Boolean
    suspend fun getGeoFencesByUserId(userId:String):List<GeoFence?>
    suspend fun deleteGeoFenceByID(geoId:Int):Boolean
    suspend fun getAllGeoFences():List<GeoFence?>
}