package com.example.jccontact.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.jccontact.home.HomeDestination
import com.example.jccontact.home.HomeScreen
import com.example.jccontact.item.ContactEditDestination
import com.example.jccontact.item.ContactEntryDestination
import com.example.jccontact.item.ContactEntryScreen
import com.example.jccontact.item.ContactItemEditScreen

@Composable
fun ConNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry =
                    { navController.navigate(ContactEntryDestination.route) },
                navigateToEditItem = { itemId ->            // Syntax: callback with argument
                    navController.navigate("${ ContactEditDestination.route}/$itemId")
                    // Meaning: go to edit screen with id (like opening news detail)
                }
            )
        }
        composable(route = ContactEntryDestination.route) {
            ContactEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        //
        composable(
            route = ContactEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ContactEditDestination.itemIdArg ){
                type = NavType.IntType                  // Syntax: require argument type = Int

            })){

            ContactItemEditScreen(
                      navigateBack = { navController.popBackStack() },
                      onNavigateUp = { navController.navigateUp() }
                )
        }

    //    ) {
      //      ContactItemEditScreen(
        //        navigateBack = { navController.popBackStack() },
          //      onNavigateUp = { navController.navigateUp() }
            //)
        //}

    }
}

