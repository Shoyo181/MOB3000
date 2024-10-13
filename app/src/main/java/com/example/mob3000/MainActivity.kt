package com.example.mob3000

import PersonListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mob3000.ui.theme.MOB3000Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MOB3000Theme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("personList") { PersonListScreen() }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        content = {
            // Bruker column for å vertikalt sentrere content
            Column(
                modifier = Modifier
                    .fillMaxSize(), // Makes the column fill the entire screen
                verticalArrangement = Arrangement.Center, // Center vertically
                horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
            ) {
                // Navigasjon kun via en knapp for nå
                Button(onClick = { navController.navigate("personList") }) {
                    Text("Gå til personliste")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Test av sirkeldiagram
                CircularProgressBar(89f, "Ja")
            }
        }
    )
}
