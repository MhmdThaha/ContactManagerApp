package com.example.contactappfb.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.contactappfb.HomeDestination
import com.example.contactappfb.HomeScreen
import com.example.contactappfb.item.ContactEditDestination
import com.example.contactappfb.item.ContactEntryDestination
import com.example.contactappfb.item.ContactEntryScreen
import com.example.contactappfb.item.ContactItemEditScreen

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
                navigateToEditItem = { itemId ->
                    navController.navigate("${ContactEditDestination.route}/$itemId")
                }
            )
        }
        composable(route = ContactEntryDestination.route) {
            ContactEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = ContactEditDestination.routeWithArgs,
            arguments = listOf(navArgument(ContactEditDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            ContactItemEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}

