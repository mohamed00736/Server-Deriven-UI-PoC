package com.yassirx.sdui_poc.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.harbin.vtcdrivertransport.ui.onBoarding.flow.onBoardingPagesFlow.OnBoardingFlowScreen
import com.yassirx.sdui_poc.ui.OnBoardingState.OnBoardingStateScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    onFinish: () -> Unit={},
    startDestination: String = "screen1",
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("screen1") {
            BackHandler(enabled = true) {
                onFinish()
            }
            OnBoardingStateScreen(onGroupSelected= {
                navController.navigate("screen2?group=$it")
            })

        }
        composable("screen2?group={group}") { backstackentry ->
            BackHandler(enabled = true) {
                onFinish()
            }
            OnBoardingFlowScreen( groupIndex = backstackentry.arguments?.getString("group")?.toIntOrNull())

        }
    }
}