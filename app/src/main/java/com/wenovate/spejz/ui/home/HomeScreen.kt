package com.wenovate.spejz.ui.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.wenovate.spejz.R
import com.wenovate.spejz.ui.grocerylist.GroceryListScreen

enum class SCREEN {
    HOME, GROCERY_LIST, ADD_GROCERY, SETTINGS, SHOPPING_LIST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

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
                    IconButton(onClick = { /* Logout functionality */ }) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
                    }
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
        GroceryListScreen()
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

private data class NavigationItemContent(
    val name: String,
    val icon: ImageVector,
    val text: String
)
