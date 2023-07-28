package com.example.domain.model

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.*

@Serializable
data class Relative(
    var name: String? = null,
    var surname: String? = null,
    var secondName: String? = null,
    val gender: String?=null,
    val birthDate: String? = null,
    var birthPlace: String? = null,
    var placeOfLiving: String? = null,
    var motherId:String?=null,
    var fatherId:String?=null,
    var photo:String?=null,
    var link:String?=null,
    var livingStatus: String?=null,
    var userId: String? = null,
    var relativeDoc:String?=null,
    @SerialName("id")
    @Contextual val id:String? = ObjectId().toHexString()
)
//
https://github.com/Rynates/Archives.git
//@Serializer(forClass = Date::class)
//object DateSerializer : KSerializer<Date> {
//    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("DateSerializer", PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: Date) {
//        encoder.encodeString(value.time.toString())
//    }
//
//    override fun deserialize(decoder: Decoder): Date {
//        return Date(decoder.decodeString())
//    }
//}