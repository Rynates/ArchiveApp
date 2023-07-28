package com.example.data

import com.example.domain.model.Archive
import com.example.domain.model.Fund
import com.example.domain.model.Document
import com.example.domain.repository.ArchiveRepository
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class ArchiveRepoImpl(
    database: CoroutineDatabase
) : ArchiveRepository {

    private val archives = database.getCollection<Archive>()
    private val funds = database.getCollection<Fund>()
    private val inventories = database.getCollection<Document>()

    override suspend fun addArchive(archive: Archive): Boolean {
        return archives.insertOne(archive).wasAcknowledged()
    }

    override suspend fun getAllArchives(): List<Archive>? {
        return archives.find().toList()
    }

    override suspend fun getArchiveById(archiveId: String): Archive? {
        return archives.findOne(filter = Archive::id eq archiveId)
    }

    override suspend fun deleteArchiveById(archiveId: String): Boolean {
        return archives.deleteOne(filter = Archive::id eq archiveId).wasAcknowledged()
    }

    override suspend fun addFund(fund: Fund): Boolean {
        return funds.insertOne(fund).wasAcknowledged()
    }

    override suspend fun addFundToArchive(archiveId:String,funds:List<Fund>): Boolean {
        return archives.updateOne(
            filter = Archive::id eq archiveId,
            update = setValue(
                property = Archive::fundList,
                value = funds
            )).wasAcknowledged()
    }

    override suspend fun deleteFundById(fundId: String): Boolean {
        return funds.deleteOne(filter = Fund::id eq fundId).wasAcknowledged()
    }

    override suspend fun getFundsByArchiveId(archiveId: String): List<Fund>? {
        return funds.find(filter = Fund::archiveId eq archiveId).toList()
    }

    override suspend fun getFundById(fundId: String): Fund? {
        return funds.findOne(filter = Fund::id eq fundId)
    }

    override suspend fun getAllFunds(): List<Fund>? {
        return funds.find().toList()
    }

    override suspend fun addInventory(document: Document): Boolean {
        return inventories.insertOne(document).wasAcknowledged()
    }

    override suspend fun addInventoryToFund(fundId:String, document:List<Document>?): Boolean {
        return funds.updateOne(
            filter = Fund::id eq fundId,
            update = setValue(
                property = Fund::documentList,
                value = document
            )).wasAcknowledged()
    }

    override suspend fun deleteInventoryById(inventoryId: String): Boolean {
        return inventories.deleteOne(filter = Document::id eq inventoryId).wasAcknowledged()
    }

    override suspend fun getInventoriesByFundId(fundId: String): List<Document>? {
        return inventories.find(filter = Document::fundId eq fundId).toList()
    }

    override suspend fun getInventoryById(inventoryId: String): Document? {
        return inventories.findOne(filter = Document::id eq inventoryId)
    }

    override suspend fun getAllInventories(): List<Document>? {
        return inventories.find().toList()
    }
}