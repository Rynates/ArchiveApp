package com.example.data

import com.example.domain.model.User
import com.example.domain.repository.UserRepository
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.set
import org.litote.kmongo.setTo


class UserRepoImpl(
    database: CoroutineDatabase
) : UserRepository {
    private val users = database.getCollection<User>()
    override suspend fun getUserByName(userName: String): User? {
        return users.findOne(filter = User::name eq userName)
    }

    override suspend fun getUserById(userId: String): User? {
        return users.findOne(filter = User::id eq userId)
    }

    override suspend fun getUserByEmail(email: String): User? {
        return users.findOne(filter = User::email eq email)
    }

    override suspend fun saveUserInfo(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun getAllUsers(): List<User> {
        return users.find().toList()
    }

    override suspend fun updateUserInfoById(userId:String, user: User): Boolean {
        return users.updateOne(
            filter = User::id eq userId,
            update = set( User::name setTo user.name, User::photo setTo user.photo,User::info setTo  user.info,User::location setTo user.location,User::isEmailOpen setTo  user.isEmailOpen,User::listOfDocuments setTo user.listOfDocuments)
        ).wasAcknowledged()
    }
}