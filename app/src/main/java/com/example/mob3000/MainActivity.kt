package com.example.mob3000

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.colorResource
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.mob3000.ui.screens.Home
import com.example.mob3000.ui.screens.PersonListeScreen
import com.example.mob3000.ui.screens.PersonTest
import com.example.mob3000.ui.screens.Sammenlign
import com.example.mob3000.ui.screens.Settings
import com.example.mob3000.ui.theme.MOB3000Theme
import com.google.firebase.auth.FirebaseAuth

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
            title = stringResource(id = R.string.nav_home),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.nav_profiles),
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.nav_compare),
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.nav_settings),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        )
    )
    var selectedNavItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val erBrukerAuthenticata = remember {mutableStateOf(auth.currentUser != null)}

    FirebaseAuth.getInstance().addAuthStateListener { auth ->
        erBrukerAuthenticata.value = auth.currentUser != null
    }
    Log.d("Loggin-Status", "Er bruker autenticata?: " + erBrukerAuthenticata.toString())

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            if (erBrukerAuthenticata.value) {
                NavigationBar(
                    containerColor = colorResource(id = R.color.dust)
                ) {
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
                                        .size(if (index == selectedNavItemIndex) 80.dp else 60.dp)
                                        //.offset(y = if(index == selectedNavItemIndex) (-10).dp else 0.dp)
                                        .background(
                                            if (index == selectedNavItemIndex) colorResource(id = R.color.dusk) else colorResource(
                                                id = R.color.dust
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
                                            Text(
                                                text = navItem.title,
                                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                            )
                                        }
                                    }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                }
            }
        }

    ) { innerPadding ->
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFEAD1BA), Color(0xFF817A81))
                    ))
                .padding(innerPadding)
        )
        NavHost (
            navController = navController,
            startDestination = if(erBrukerAuthenticata.value) "Home" else "SwipeLanding"

        ) {
            composable("SwipeLanding") {
                SwipeLandingsside(
                    onLoginSuccess = {
                        navController.navigate("Home") {
                            popUpTo("SwipeLanding") { inclusive = true }
                        }
                    }
                )
            }
            composable("Home") {
                Home(Modifier.padding(innerPadding))
            }
            composable("Profiles") {
                PersonListeScreen(Modifier.padding(innerPadding),
                    navController = navController
                )
            }
            composable("Compare") {
                Sammenlign(Modifier.padding(innerPadding))
            }
            composable("Settings") {
                Settings(Modifier.padding(innerPadding))
            }
            composable(
                route = "persontest/{testId}/{name}",
                arguments = listOf(
                    navArgument("testId") {type = NavType.StringType},
                    navArgument("name") {type = NavType.StringType}
                )
            ) {backStackEntry ->
                val testId = backStackEntry.arguments?.getString("testId")
                val name = backStackEntry.arguments?.getString("name")?.let { Uri.decode(it) } ?: ""
                PersonTest(testId = testId, name = name)
            }
        }
    }
}

