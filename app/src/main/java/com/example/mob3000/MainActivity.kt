package com.example.mob3000

import Home
import PersonListScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.unit.dp
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

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)


@Composable
fun MyApp() {
    val navItems = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Profiler",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        ),
        BottomNavigationItem(
            title = "Sammenlign",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )
    var selectedNavItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val navController = rememberNavController()
/*
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("personList") { PersonListScreen() }
    }*/
    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = colorResource(id = R.color.white)
            ){
                navItems.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedNavItemIndex == index,
                        onClick = {
                            selectedNavItemIndex = index
                            navController.navigate(navItem.title)
                            println(navItem.title)
                        },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(if (index == selectedNavItemIndex)80.dp else 60.dp)
                                    //.offset(y = if(index == selectedNavItemIndex) (-10).dp else 0.dp)
                                    .background(
                                        if (index == selectedNavItemIndex) colorResource(id = R.color.krem) else colorResource(
                                            id = R.color.white
                                        ),
                                        shape = RoundedCornerShape(size = 20.dp)

                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = navItem.selectedIcon,
                                        contentDescription = navItem.title,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .background(Color.Transparent)
                                    )
                                    if (index == selectedNavItemIndex) {
                                    Text(
                                        text = navItem.title,
                                        style = MaterialTheme.typography.bodyMedium)
                                }
                                }
                            }
                        },
                            label = {
                                if(index != selectedNavItemIndex) {
                                    Text(
                                        text = navItem.title,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }

    ) { innerPadding ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.krem))
                .padding(innerPadding)
        )
        NavHost (
            navController = navController,
            startDestination = "Home"

        ) {
            composable("Home") {
                Home(Modifier.padding(innerPadding))
            }
            composable("Profiler") {
                PersonListScreen(Modifier.padding(innerPadding))
            }
            /*composable("Sammenlign") {
                Sammenlign(Modifier.padding(innerPadding)
            }*/
        }
    }

}


@Composable
fun Tema(content: @Composable () -> Unit) {
    val image = painterResource(id = R.drawable.bilde_1) // Make sure the image resource exists
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = image,
            contentDescription = "Bakgrunnsbilde", // Removed unnecessary parentheses
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )

    }
}

/*
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
    ) {}
}
*/
