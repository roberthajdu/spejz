package com.wenovate.spejz.data.repository

import com.wenovate.spejz.data.datasource.UserDataSource
import com.wenovate.spejz.data.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {

    suspend fun getUser(userId: String): User? {
        return userDataSource.getUser(userId)
    }

    suspend fun addUser(user: User) {
        userDataSource.addUser(user)
    }
}