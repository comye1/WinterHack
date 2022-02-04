package com.comye1.wewon.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Read : Screen("read")
    object Writings : Screen("writings")
    object Sentences : Screen("sentences")
    object Words : Screen("words")
    object LogIn : Screen("login")
    object SignUp : Screen("signup")
    object Voca : Screen("voca")
    object Category : Screen("category")
    object Random: Screen("random")
}