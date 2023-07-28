package com.example.data

import com.example.domain.model.GeoFence
import com.example.domain.repository.GeoFenceRepository
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

class GeoFenceRepoImpl(
    database:CoroutineDatabase
):GeoFenceRepository {

    private val geofences = database.getCollection<GeoFence>()

    override suspend fun addGeoFence(geoFence: GeoFence): Boolean {
        return geofences.insertOne(document = geoFence).wasAcknowledged()
    }

    override suspend fun getGeoFencesByUserId(userId: String): List<GeoFence?> {
        return geofences.find(filter= GeoFence::createdBy eq userId).toList()
    }

    override suspend fun deleteGeoFenceByID(geoId: Int): Boolean {
        return geofences.deleteOne(filter = GeoFence::geoId eq geoId).wasAcknowledged()
    }

    override suspend fun getAllGeoFences(): List<GeoFence?> {
        return geofences.find().toList()
    }
}