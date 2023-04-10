package com.example.composepractice.view

import android.view.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composepractice.R
import com.example.composepractice.ui.theme.BloomTheme

object TouristGuide {
    var navController: NavHostController? = null
        set(value) {
            field = value
        }
    fun toWelcome() {
        navController?.navigate(RouterPath.WELCOME) {
            anim {
                this.enter = R.anim.slide_in_right
                this.exit = R.anim.slide_out_left
                this.popEnter = R.anim.slide_in_left
                this.popExit = R.anim.slide_out_right
            }
            launchSingleTop = true
        }
    }
    fun toLogin() {
        navController?.navigate(RouterPath.LOGIN) {
            anim {
                this.enter = R.anim.slide_in_right
                this.exit = R.anim.slide_out_left
                this.popEnter = R.anim.slide_in_left
                this.popExit = R.anim.slide_out_right
            }
            launchSingleTop = true
        }
    }
    fun toHome() {
        navController?.navigate(RouterPath.HOME) {
            anim {
                this.enter = R.anim.slide_in_right
                this.exit = R.anim.slide_out_left
                this.popEnter = R.anim.slide_in_left
                this.popExit = R.anim.slide_out_right
            }
            launchSingleTop = true
        }
    }
}

object RouterPath {
    val WELCOME = "welcome"
    val LOGIN = "login"
    val HOME = "home"
}

//@Composable
//fun NavGraph(window: Window) {
//    val theme by remember { mutableStateOf(BloomTheme.DARK) }
//    val navHostController = rememberNavController()
//    TouristGuide.navController = navHostController
//    NavHost(navController = navHostController, startDestination = RouterPath.WELCOME) {
//        composable(RouterPath.WELCOME) {
//            window.statusBarColor = MaterialTheme.colors.primary.toArgb()
//            BloomTheme(theme) {
//                WelcomePage {
//                    navHostController.popBackStack()
//                }
//            }
//        }
//        composable(RouterPath.LOGIN) {
//            window.statusBarColor = MaterialTheme.colors.background.toArgb()
//            BloomTheme(theme) {
//                LoginPage {
//                    navHostController.popBackStack()
//                }
//            }
//        }
//        composable(RouterPath.HOME) {
//            window.statusBarColor = MaterialTheme.colors.background.toArgb()
//            BloomTheme(theme) {
//                HomePage {
//                    navHostController.popBackStack()
//                }
//            }
//        }
//    }
//}