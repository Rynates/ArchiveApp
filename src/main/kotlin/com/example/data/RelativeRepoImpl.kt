package com.example.data

import com.example.domain.model.Relative
import com.example.domain.repository.RelativeRepository
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.set
import org.litote.kmongo.setTo

class RelativeRepoImpl(
    database: CoroutineDatabase
) : RelativeRepository {

    private val relatives = database.getCollection<Relative>()

    override suspend fun addRelative(relative: Relative): Boolean {
        return relatives.insertOne(relative).wasAcknowledged()
    }

    override suspend fun getRelativesByUserId(userId: String): List<Relative> {
        return relatives.find(filter = Relative::userId eq userId).toList()
    }

    override suspend fun getRelativeById(relativeId: String): Relative? {
        return relatives.findOne(filter = Relative::id eq relativeId)
    }

    override suspend fun deleteRelativeById(relativeId: String): Boolean {
        val relativeUid = Relative::id
        return relatives.deleteOne(filter = relativeUid eq relativeId).wasAcknowledged()
    }

    override suspend fun updateRelativeById(relative: Relative): Boolean {
        return relatives.updateOne(
            filter = Relative::id eq relative.id,
            update = set(
                Relative::name setTo relative.name,
                Relative::surname setTo relative.surname,
                Relative::secondName setTo relative.secondName,
                Relative::birthPlace setTo relative.birthPlace,
                Relative::placeOfLiving setTo relative.placeOfLiving,
                Relative::livingStatus setTo relative.livingStatus,
                Relative::birthDate setTo relative.birthDate,
                Relative::photo setTo relative.photo,
                Relative::fatherId setTo relative.fatherId,
                Relative::motherId setTo relative.motherId,
                Relative::link setTo relative.link,
                Relative::relativeDoc setTo relative.relativeDoc
            )
        ).wasAcknowledged()
    }

    override suspend fun getAllRelatives(): List<Relative>? {
        return relatives.find().toList()
    }
}