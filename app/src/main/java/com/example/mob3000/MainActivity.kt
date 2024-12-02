package com.example.mob3000

import android.net.Uri
import android.os.Bundle
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

/**
 * MainActivity er hovedaktiviteten i appen.
 * Setter igang applikasjonen med valgt tema
 */
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

/**
 * Hjelpe data klasse for å holde styr på destinasjoner som er i nav-baren
 * title er det som syntes i navbaren, men route er den faktiske destinasjonen
 */
data class BottomNavigationItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector
)

/**
 * Komponenten håndterer navigasjon for applikasjonen og viser frem de skjermene/Screens som
 * bruker velger, ved hjelp av nav-bar. Sjekker først om bruker er innlogget, hvis ikke
 * navigeres bruker til innloggingsside/SwipeLandingsside.kt
 * Bruker NavHost og NavController fra Jetpack Compose for navigasjon.
 * Bruker NavigationBar og NavigationBarItem fra Material Design for navigasjonsbaren.
 *
 * Funksjoner
 * - Navigasjon
 * - Prøver å håndtere navigasjonsstakken, til beste evne (er ikke optimalt)
 * - Sjekker om bruker er innlogget
 * - behandler logikk på tilbake knapp i launchedEffect
 *
 */
@Composable
fun MyApp() {
    val navItems = listOf(
        BottomNavigationItem(
            title = stringResource(id = R.string.nav_home),
            route = "Hjem",
            selectedIcon = Icons.Filled.Home,
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.nav_profiles),
            route = "Profiler",
            selectedIcon = Icons.Filled.Person
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.nav_compare),
            route = "Sammenlign",
            selectedIcon = Icons.Filled.Favorite
        ),
        BottomNavigationItem(
            title = stringResource(id = R.string.nav_settings),
            route = "Settings",
            selectedIcon = Icons.Filled.Settings
        )
    )

    var selectedNavItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val navController = rememberNavController()

    // Observer ruten og oppdater indeksen
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val route = backStackEntry.destination.route
            val navItemIndex = navItems.indexOfFirst { it.route == route }

            // Oppdater bare indeksen hvis ruten er en del av navItems
            if (navItemIndex >= 0) {
                selectedNavItemIndex = navItemIndex
            } else if (route == "Persontest/{testId}/{name}") {
                // Oppdater indeksen manuelt for "Profiles" når brukeren er i en route relatert til "Profiles"
                selectedNavItemIndex = navItems.indexOfFirst { it.route == "Profiler" }
            } else {
                // Sett til en nøytral verdi for ruter utenfor NavigationBar
                selectedNavItemIndex = -1
            }
        }
    }

    val auth = FirebaseAuth.getInstance()
    val erBrukerAuthenticata = remember {mutableStateOf(auth.currentUser != null)}

    auth.addAuthStateListener { a ->
        erBrukerAuthenticata.value = a.currentUser != null
    }

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
                                if (selectedNavItemIndex != index) {
                                    selectedNavItemIndex = index
                                    navController.navigate(navItem.route){
                                        if (navItem.route in navItems.map { it.route }) {
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                                inclusive = false // Ikke fjern startdestinasjonen
                                            }
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                    println(navItem.route)
                                }
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
                        colors = listOf(colorResource(id = R.color.sand), (colorResource(id = R.color.dusk)))
                    ))
                .padding(innerPadding)
        )
        NavHost (
            navController = navController,
            startDestination = if(erBrukerAuthenticata.value) "Hjem" else "SwipeLanding"

        ) {
            composable("SwipeLanding") {
                SwipeLandingsside(
                    onLoginSuccess = {
                        navController.navigate("Hjem") {
                            popUpTo("SwipeLanding") { inclusive = true }
                        }
                    }
                )
            }
            composable("Hjem") {
                Home(Modifier.padding(innerPadding))
            }
            composable("Profiler") {
                PersonListeScreen(Modifier.padding(innerPadding),
                    navController = navController
                )
            }
            composable("Sammenlign") {
                Sammenlign(Modifier.padding(innerPadding))
            }
            composable("Settings") {
                Settings(Modifier.padding(innerPadding))
            }
            composable(
                route = "Persontest/{testId}/{name}",
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

