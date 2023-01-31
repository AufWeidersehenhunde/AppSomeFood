package com.example.appsomefood.MainActivity

import androidx.lifecycle.ViewModel
import com.example.appsomefood.Screens
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryUser
import com.github.terrakok.cicerone.Router

class MainActivityViewModel(
    private val router: Router,
    private val repositoryUser: RepositoryUser
) : ViewModel(){

    fun create(){
        if (repositoryUser.pref.isNullOrEmpty()) {
            router.newRootScreen(Screens.routeToHomeFragment())
        }else{
            router.newRootScreen(Screens.routeToFragmentContainer())
        }
    }
}