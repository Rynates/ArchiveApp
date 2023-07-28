package com.example.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.newId


@Serializable
data class User(
    @Transient val name:String?=null,
    @Transient val email:String?= null,
    @Transient val location:String?=null,
    @Transient val info:String?=null,
    @Transient val password:String?=null,
    @Transient val salt:String?=null,
    @Transient var photo:String?=null,
    @Transient var isEmailOpen:Boolean = true,
    @Transient var listOfDocuments:List<Document> = emptyList(),
    @SerialName("id")
    @Contextual val id:String? = ObjectId().toHexString()
)
