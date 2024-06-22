package com.wenovate.spejz.data.repository

import com.wenovate.spejz.data.datasource.GroceryDataSource
import com.wenovate.spejz.data.model.Grocery
import com.wenovate.spejz.data.model.Household
import javax.inject.Inject

class GroceryRepository @Inject constructor(private val groceryDataSource: GroceryDataSource) {

    suspend fun getGroceries(householdId: String): List<Grocery> {
        return groceryDataSource.getGroceries(householdId)
    }

    suspend fun addGrocery(grocery: Grocery) {
        groceryDataSource.addGrocery(grocery)
    }

    suspend fun getHouseholds(userId: String): List<Household> {
        return groceryDataSource.getHouseholds(userId)
    }

    suspend fun addHousehold(household: Household): String {
        return groceryDataSource.addHousehold(household)
    }

    suspend fun joinHousehold(householdId: String, userId: String) {
        groceryDataSource.joinHousehold(householdId, userId)
    }
}