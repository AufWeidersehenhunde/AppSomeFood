package com.example.appsomefood.MainActivity

import androidx.lifecycle.ViewModel
import com.example.somefood.Navigation.Screens
import com.example.appsomefood.repository.RepositoryUser
import com.github.terrakok.cicerone.Router

class MainActivityViewModel(
    private val router: Router,
    private val repositoryUser: RepositoryUser
) : ViewModel() {

    fun create() {
        repositoryUser.getPreference()
        if (repositoryUser.userID.isNullOrEmpty()) {
            router.newRootScreen(Screens.routeToHomeFragment())
        } else {
            router.newRootScreen(Screens.routeToFragmentContainer())
        }
    }
}