package com.wenovate.spejz.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.wenovate.spejz.data.model.Grocery
import com.wenovate.spejz.data.model.Household
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroceryDataSource @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun getGroceries(householdId: String): List<Grocery> {
        return firestore.collection("groceries")
            .whereEqualTo("householdId", householdId)
            .get()
            .await()
            .toObjects(Grocery::class.java)
    }

    suspend fun addGrocery(grocery: Grocery) {
        firestore.collection("groceries")
            .add(grocery)
            .await()
    }

    suspend fun getHouseholds(userId: String): List<Household> {
        return firestore.collection("households")
            .whereArrayContains("members", userId)
            .get()
            .await()
            .toObjects(Household::class.java)
    }

    suspend fun addHousehold(household: Household): String {
        val docRef = firestore.collection("households").add(household).await()
        return docRef.id
    }

    suspend fun joinHousehold(householdId: String, userId: String) {
        val householdRef = firestore.collection("households").document(householdId)
        val household = householdRef.get().await().toObject(Household::class.java)
        household?.let {
            val updatedMembers = it.members.toMutableList().apply { add(userId) }
            householdRef.update("members", updatedMembers).await()
        }
    }
}