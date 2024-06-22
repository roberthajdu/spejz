package com.wenovate.spejz.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.wenovate.spejz.data.model.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getUser(userId: String): User? {
        return firestore.collection("users")
            .document(userId)
            .get()
            .await()
            .toObject(User::class.java)
    }

    suspend fun addUser(user: User) {
        firestore.collection("users")
            .document(user.id)
            .set(user)
            .await()
    }
}