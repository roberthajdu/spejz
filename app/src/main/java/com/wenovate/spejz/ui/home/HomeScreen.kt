package com.wenovate.spejz.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wenovate.spejz.R
import com.wenovate.spejz.ui.grocerylist.GroceryListScreen
import com.wenovate.spejz.ui.grocerylist.GroceryViewModel

enum class SCREEN {
    HOME, GROCERY_LIST, ADD_GROCERY, SETTINGS, SHOPPING_LIST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController,
               viewModel: GroceryViewModel = hiltViewModel()) {

    val households by viewModel.households.collectAsState()
    val currentHouseholdId by viewModel.currentHouseholdId.collectAsState()
    var showCreateHouseholdDialog by remember { mutableStateOf(false) }
    var showJoinHouseholdDialog by remember { mutableStateOf(false) }

    val navigationItemContentList = listOf(
        NavigationItemContent(
            name = SCREEN.HOME.name,
            icon = Icons.Default.Home,
            text = "Spejz"
        ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spåjz") },
                actions = {
                    IconButton(onClick = { showCreateHouseholdDialog = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Create Household")
                    }
//                    IconButton(onClick = { showJoinHouseholdDialog = true }) {
//                        Icon(imageVector = Icons.Default.Group, contentDescription = "Join Household")
//                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                ReplyBottomNavigationBar(
                    currentTab = SCREEN.HOME,
                    onTabPressed = { /* Handle tab press */ },
                    navigationItemContentList = navigationItemContentList,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Add Grocery functionality */ }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Grocery")
            }
        }
    ) {
        currentHouseholdId?.let {
            GroceryListScreen(householdId = it)
        } ?: run {
            Text("No household selected")
        }
    }

    if (showCreateHouseholdDialog) {
        CreateHouseholdDialog(onDismiss = { showCreateHouseholdDialog = false }, onCreate = { name ->
            viewModel.createHousehold(name)
            showCreateHouseholdDialog = false
        })
    }

    if (showJoinHouseholdDialog) {
        JoinHouseholdDialog(onDismiss = { showJoinHouseholdDialog = false }, onJoin = { householdId ->
            viewModel.joinHousehold(householdId)
            showJoinHouseholdDialog = false
        })
    }
}

@Composable
private fun ReplyBottomNavigationBar(
    currentTab: SCREEN,
    onTabPressed: ((String) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab.name == navItem.name,
                onClick = { onTabPressed(navItem.name) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}

@Composable
fun CreateHouseholdDialog(onDismiss: () -> Unit, onCreate: (String) -> Unit) {
    var name by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Household") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Household Name") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onCreate(name) }) {
                Text("Create")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun JoinHouseholdDialog(onDismiss: () -> Unit, onJoin: (String) -> Unit) {
    var householdId by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Join Household") },
        text = {
            Column {
                OutlinedTextField(
                    value = householdId,
                    onValueChange = { householdId = it },
                    label = { Text("Household ID") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onJoin(householdId) }) {
                Text("Join")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

private data class NavigationItemContent(
    val name: String,
    val icon: ImageVector,
    val text: String
)
