package com.hantash.echojournal.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.hantash.echojournal.echo.presentation.echo_create.EchoCreateRoot
import com.hantash.echojournal.echo.presentation.echo_list.EchoRoot
import com.hantash.echojournal.echo.presentation.settings.SettingsRoot
import com.hantash.echojournal.echo.presentation.util.toCreateEchoRoute

const val ACTION_CREATE_ECHO = "com.plcoding.CREATE_ECHO"

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.EchoScreen(
            startRecording = false
        )
    ) {
        composable<NavigationRoute.EchoScreen>(
            deepLinks = listOf(
                navDeepLink<NavigationRoute.EchoScreen>(
                    basePath = "https://echojournal.com/echos"
                ) {
                    action = ACTION_CREATE_ECHO
                }
            )
        ) {
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