package com.example.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class GeoFence(
    val geoId:Int?=null,
    val name:String?=null,
    val latitude:Double?=null,
    val longitude:Double?=null,
    val location:String?=null,
    val time:String?=null,
    val year:String?=null,
    val radius:Double?=null,
    val createdBy:String?=null,
    @SerialName("id")
    @Contextual val id:String? = ObjectId().toHexString()
)
