package com.example.appsomefood.fragmentContainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Screens
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryProfileData
import com.example.appsomefood.repository.RepositorySQL
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ContainerViewModel(
    private val router: Router,
    private val repositorySQL: RepositorySQL,
    private val repositoryProfileData: RepositoryProfileData,
    preference: Reference
) : ViewModel() {
    val statusProfile = MutableStateFlow<UsersDb?>(null)
    val user = preference.getValue("pref").toString()

    fun create() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repositorySQL.checkStatus(user)?.isCreator == true) {
                router.newRootScreen(Screens.routeToCreatorList())
            }else{
                router.newRootScreen(Screens.routeToListFragment())
            }
        }
    }

    fun checkAccount(){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.checkAccountForStatus(user)?.filterNotNull()?.collect{
                    statusProfile.value = it
            }
        }
    }

    fun routeToProfile(){
        router.navigateTo((Screens.routeToProfileFragment()))
    }

    fun goToHomeForCreator(){
        router.newRootScreen(Screens.routeToCreatorList())
    }

    fun goBack(){
        router.newRootScreen(Screens.routeToListFragment())
    }

    fun routeToFavorite(){
        router.newRootScreen(Screens.routeToFavoriteFragment())
    }

    fun routeToOrders(){
        router.newRootScreen(Screens.routeToOrdersFragment())
    }

    fun routeToOrdersWork(){
        router.newRootScreen(Screens.routeToOrdersWork())
    }
}