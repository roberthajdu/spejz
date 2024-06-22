package com.wenovate.spejz.ui.grocerylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.wenovate.spejz.data.model.Grocery
import com.wenovate.spejz.data.model.Household
import com.wenovate.spejz.data.model.User
import com.wenovate.spejz.data.repository.GroceryRepository
import com.wenovate.spejz.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroceryViewModel @Inject constructor(
    private val groceryRepository: GroceryRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _groceries = MutableStateFlow<List<Grocery>>(emptyList())
    val groceries: StateFlow<List<Grocery>> get() = _groceries

    private val _households = MutableStateFlow<List<Household>>(emptyList())
    val households: StateFlow<List<Household>> get() = _households

    private val _currentHouseholdId = MutableStateFlow<String?>(null)
    val currentHouseholdId: StateFlow<String?> get() = _currentHouseholdId

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    init {
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            val user = userRepository.getUser(userId)
            if (user == null) {
                // Add user if not exists
                val newUser = User(
                    id = userId,
                    name = FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    email = FirebaseAuth.getInstance().currentUser?.email ?: ""
                )
                userRepository.addUser(newUser)
                _user.value = newUser
            } else {
                _user.value = user
            }
            fetchHouseholds()
        }
    }

    private fun fetchHouseholds() {
        viewModelScope.launch {
            val userId = _user.value?.id ?: return@launch
            val householdList = groceryRepository.getHouseholds(userId)
            _households.value = householdList
            if (householdList.isNotEmpty()) {
                _currentHouseholdId.value = householdList.first().id
                fetchGroceries(householdList.first().id)
            }
        }
    }

    fun fetchGroceries(householdId: String) {
        viewModelScope.launch {
            val groceryList = groceryRepository.getGroceries(householdId)
            _groceries.value = groceryList
        }
    }

    fun addGrocery(grocery: Grocery) {
        viewModelScope.launch {
            groceryRepository.addGrocery(grocery)
            currentHouseholdId.value?.let { fetchGroceries(it) }
        }
    }

    fun createHousehold(name: String) {
        viewModelScope.launch {
            val userId = _user.value?.id ?: return@launch
            val household = Household(name = name, members = listOf(userId))
            val householdId = groceryRepository.addHousehold(household)
            _currentHouseholdId.value = householdId
            fetchHouseholds()
        }
    }

    fun joinHousehold(householdId: String) {
        viewModelScope.launch {
            val userId = _user.value?.id ?: return@launch
            groceryRepository.joinHousehold(householdId, userId)
            _currentHouseholdId.value = householdId
            fetchHouseholds()
        }
    }

    fun switchHousehold(householdId: String) {
        _currentHouseholdId.value = householdId
        fetchGroceries(householdId)
    }
}