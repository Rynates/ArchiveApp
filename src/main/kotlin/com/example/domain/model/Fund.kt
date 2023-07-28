package com.example.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Fund(
    val number:Int,
    val name:String,
    var documentList:List<Document>? = arrayListOf(),
    var inventoryNum:String?=null,
    val archiveId:String,
    val digitization:String?=null,
    @SerialName("id")
    @Contextual val id:String? = ObjectId().toHexString()
)
