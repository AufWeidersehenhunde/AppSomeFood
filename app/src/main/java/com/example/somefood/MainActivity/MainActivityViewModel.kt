package com.example.appsomefood.MainActivity

import androidx.lifecycle.ViewModel
import com.example.appsomefood.Screens
import com.example.appsomefood.repository.Reference
import com.github.terrakok.cicerone.Router

class MainActivityViewModel(
    private val router: Router,
    preference:Reference
) : ViewModel(){
    val pref = preference.getValue("pref")

    fun create(){
        if (pref.isNullOrEmpty()) {
            router.newRootScreen(Screens.routeToHomeFragment())
        }else{
            router.newRootScreen(Screens.routeToFragmentContainer())
        }
    }
}