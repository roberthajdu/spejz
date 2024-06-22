package com.wenovate.spejz.data.repository

import com.wenovate.spejz.data.datasource.GroceryDataSource
import com.wenovate.spejz.data.model.Grocery
import javax.inject.Inject

class GroceryRepository @Inject constructor(private val groceryDataSource: GroceryDataSource) {

    suspend fun getGroceries(): List<Grocery> {
        return groceryDataSource.getGroceries()
    }

    suspend fun addGrocery(grocery: Grocery) {
        groceryDataSource.addGrocery(grocery)
    }
}