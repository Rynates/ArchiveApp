package com.example.domain.repository

import com.example.domain.model.User

interface UserRepository {
    suspend fun getUserByName(userName:String):User?
    suspend fun getUserById(userId: String): User?
    suspend fun getUserByEmail(email:String):User?
    suspend fun saveUserInfo(user:User):Boolean
    suspend fun getAllUsers():List<User>
    suspend fun updateUserInfoById(
        userId: String,
        user:User
    ):Boolean
}