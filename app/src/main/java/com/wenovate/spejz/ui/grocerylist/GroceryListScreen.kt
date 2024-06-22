package com.wenovate.spejz.ui.grocerylist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.wenovate.spejz.data.model.Grocery

@Composable
fun GroceryListScreen(
    viewModel: GroceryViewModel = hiltViewModel()
) {
    var groceryName by remember { mutableStateOf("") }
    val groceries by viewModel.groceries.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = groceryName,
            onValueChange = { groceryName = it },
            label = { Text("Grocery Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.addGrocery(Grocery(name = groceryName))
                groceryName = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Grocery")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(groceries) { grocery ->
                Text(text = grocery.name)
            }
        }
    }
}