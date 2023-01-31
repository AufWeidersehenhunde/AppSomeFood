package com.example.appsomefood.AuthAndAuthorize

import androidx.lifecycle.ViewModel
import com.example.appsomefood.Screens
import com.github.terrakok.cicerone.Router

class AuthAndRegViewModel(
private val router: Router
) : ViewModel() {

   fun routeToReg(){
       router.navigateTo(Screens.routeToRegistrationFragment())

   }
    fun routeToAuth(){
        router.navigateTo(Screens.routeToAuthFragment())
    }
}