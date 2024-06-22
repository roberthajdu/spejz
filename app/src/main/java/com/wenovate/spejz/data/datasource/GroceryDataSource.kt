package com.wenovate.spejz.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.wenovate.spejz.data.model.Grocery
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroceryDataSource @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun getGroceries(): List<Grocery> {
        val snapshot = firestore.collection("groceries").get().await()
        return snapshot.documents.map { it.toObject(Grocery::class.java)!! }
    }

    suspend fun addGrocery(grocery: Grocery) {
        firestore.collection("groceries").add(grocery).await()
    }
}