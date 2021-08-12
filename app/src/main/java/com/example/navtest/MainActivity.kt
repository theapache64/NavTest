package com.example.navtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.navtest.ui.theme.NavTestTheme


data class BottomMenuItem(
    val counterId: String,
    val title: String
)

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Building tabs
        val menuItems: List<BottomMenuItem> = mutableListOf<BottomMenuItem>().apply {
            repeat(3) {
                add(
                    BottomMenuItem(
                        counterId = "counter_$it",
                        title = "Counter $it"
                    )
                )
            }
        }

        setContent {
            NavTestTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()

                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(
                        bottomBar = {
                            BottomMenu(menuItems) {
                                // menu clicked
                                navController.navigate("counter/${it.counterId}") {
                                    // launchSingleTop = true
                                }
                            }
                        }
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = "counter/{counterId}"
                        ) {
                            composable(
                                route = "counter/{counterId}",
                                arguments = listOf(navArgument("counterId") {
                                    defaultValue = menuItems.first().counterId
                                })
                            ) {
                                val counterId = it.arguments!!.getString("counterId")!!
                                CounterScreen(counterId = counterId)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomMenu(
        menuItems: List<BottomMenuItem>,
        onMenuItemClicked: (BottomMenuItem) -> Unit
    ) {

        BottomNavigation {
            for (menuItem in menuItems) {
                BottomNavigationItem(
                    selected = false,
                    onClick = {
                        onMenuItemClicked(menuItem)
                    },
                    icon = { Icon(imageVector = Icons.Outlined.Warning, contentDescription = "") },
                    label = { Text(text = menuItem.title) }
                )
            }
        }
    }

    @Composable
    private fun CounterScreen(counterId: String) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "CounterId: '$counterId'", fontSize = 25.sp)

            var count by rememberSaveable { mutableStateOf(0) }
            Button(onClick = { count++ }) {
                Text(text = "$count")
            }
        }
    }
}
