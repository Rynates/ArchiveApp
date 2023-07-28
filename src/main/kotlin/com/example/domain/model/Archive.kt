package com.example.domain.model

import com.google.gson.annotations.Expose
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class Archive(
    var name:String? = null,
    var city:String? = null,
    var type:String? = null,
    var phone:String? = null,
    var address:String? = null,
    var email:String? = null,
    var fundList:List<Fund>? = arrayListOf(),
    @Expose(serialize = false, deserialize = false)
    var isExpandable:Boolean = true,
    @SerialName("id")
    @Contextual val id:String? = ObjectId().toHexString()
)
