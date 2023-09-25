package dev.haqim.findyourpokemon

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.haqim.catapp.ui.screen.navigation.Screen
import dev.haqim.findyourpokemon.ui.component.PlainMessage
import dev.haqim.findyourpokemon.ui.detail.DetailPokemonScreen
import dev.haqim.findyourpokemon.ui.list.PokemonListScreen
import dev.haqim.findyourpokemon.ui.theme.FindYourPokemonTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FindYourPokemonTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PokemonApp()
                }
            }
        }
    }
}

private const val ID = "id"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }

    Scaffold(
        topBar = {
            if(currentRoute != Screen.DetailPokemon.route){
                TopAppBar(
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    title = { 
                        val title = if(currentRoute == Screen.PokemonList.route){
                            stringResource(id = R.string.app_name)
                        }else{
                            stringResource(id = R.string.owned_pokemon)
                        }
                        Text(
                            text = title
                        )
                    },
                    actions = {
                        if(currentRoute == Screen.PokemonList.route){
                            IconButton(onClick = {
                                navController.navigate(
                                    Screen.OwnedPokemonList.route
                                )
                            }) {
                                Icon(
                                    painterResource(id = R.drawable.pokeball),
                                    modifier = Modifier.rotate(rotation.value),
                                    contentDescription = "See owned pokemon",
                                    tint = Color.Unspecified
                                )
                                LaunchedEffect(Unit){
                                    scope.launch {
                                        while(true){
                                            rotation.animateTo(
                                                targetValue = 180f,
                                                animationSpec = tween(1000, easing = LinearEasing)
                                            )
                                            rotation.animateTo(
                                                targetValue = 360f,
                                                animationSpec = tween(1000, easing = LinearEasing)
                                            )
                                            rotation.snapTo(0f)
                                        }
                                    }
                                }
                            }
                        }else if(currentRoute == Screen.OwnedPokemonList.route){
                            IconButton(onClick = {
                                navController.navigate(
                                    Screen.PokemonList.route
                                )
                            }) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Find pokemon",
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                )
            }
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.PokemonList.route,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(Screen.PokemonList.route) {
                PokemonListScreen(
                    navController = navController
                )
            }
            composable(Screen.OwnedPokemonList.route){
                PokemonListScreen(
                    navController = navController,
                    ownedOnly = true
                )
            }
            composable(
                Screen.DetailPokemon.route,
                arguments = listOf(navArgument(ID){type = NavType.StringType})
            ){
                val id = it.arguments?.getString(ID)
                if (id != null) {
                    DetailPokemonScreen(
                        id = id,
                        navigateBack = { navController.navigateUp() }
                    )
                }else{
                    PlainMessage(message = "ID not found")
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FindYourPokemonTheme() {
        PokemonApp()
    }
}