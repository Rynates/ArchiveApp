package com.example.domain.model

import com.google.gson.annotations.Expose
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Document(
    val name:String,
    val number:Int,
    val fundId:String,
    val userId:String?=null,
    val archiveId:String?=null,
    @Contextual val id:String? = ObjectId().toHexString()
)
