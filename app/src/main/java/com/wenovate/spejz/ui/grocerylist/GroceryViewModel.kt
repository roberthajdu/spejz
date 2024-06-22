package com.wenovate.spejz.ui.grocerylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wenovate.spejz.data.model.Grocery
import com.wenovate.spejz.data.repository.GroceryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository
) : ViewModel() {

    private val _groceries = MutableLiveData<List<Grocery>>()
    val groceries: LiveData<List<Grocery>> get() = _groceries

    fun loadGroceries() {
        viewModelScope.launch {
            val result = groceryRepository.getGroceries()
            _groceries.value = result
        }
    }

    fun addGrocery(grocery: Grocery) {
        viewModelScope.launch {
            groceryRepository.addGrocery(grocery)
            loadGroceries() // Refresh the list after adding
        }
    }
}