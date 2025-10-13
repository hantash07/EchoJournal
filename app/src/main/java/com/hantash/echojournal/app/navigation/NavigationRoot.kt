package com.hantash.echojournal.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hantash.echojournal.echo.presentation.echo_create.EchoCreateRoot
import com.hantash.echojournal.echo.presentation.echo_list.EchoRoot
import com.hantash.echojournal.echo.presentation.settings.SettingsRoot
import com.hantash.echojournal.echo.presentation.util.toCreateEchoRoute

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.EchoScreen
    ) {
        composable<NavigationRoute.EchoScreen> {
            EchoRoot(
                onNavToCreateEcho = { details ->
                    navController.navigate(details.toCreateEchoRoute())
                },
                onNavToSettings = {
                    navController.navigate(NavigationRoute.SettingsScreen)
                }
            )
        }
        composable<NavigationRoute.CreateEchoScreen> {
            EchoCreateRoot(
                onConfirmLeave = navController::navigateUp
            )
        }
        composable<NavigationRoute.SettingsScreen> {
            SettingsRoot(
                onGoBack = navController::navigateUp
            )
        }
    }
}