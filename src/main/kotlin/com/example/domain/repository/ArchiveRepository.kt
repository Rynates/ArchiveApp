package com.example.domain.repository

import com.example.domain.model.Archive
import com.example.domain.model.Fund
import com.example.domain.model.Document

interface ArchiveRepository {
    suspend fun addArchive(archive:Archive):Boolean
    suspend fun getAllArchives():List<Archive>?
    suspend fun getArchiveById(archiveId:String):Archive?
    suspend fun deleteArchiveById(archiveId:String):Boolean
    suspend fun addFund(fund: Fund):Boolean
    suspend fun addFundToArchive(archiveId:String,funds:List<Fund>):Boolean
    suspend fun deleteFundById(fundId:String):Boolean
    suspend fun getAllFunds(): List<Fund>?
    suspend fun getFundsByArchiveId(archiveId: String):List<Fund>?
    suspend fun getFundById(fundId:String):Fund?

    suspend fun addInventory(document: Document):Boolean
    suspend fun addInventoryToFund(fundId:String,inventories: List<Document>?):Boolean
    suspend fun deleteInventoryById(inventoryId:String):Boolean
    suspend fun getAllInventories(): List<Document>?
    suspend fun getInventoriesByFundId(fundId: String):List<Document>?
    suspend fun getInventoryById(inventoryId:String):Document?
}