package com.example.appsomefood.fragmentContainer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Screens
import com.example.appsomefood.repository.RepositoryProfileData
import com.example.appsomefood.repository.RepositoryUser
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ContainerViewModel(
    private val router: Router,
    private val repositoryUser: RepositoryUser,
    private val repositoryProfileData: RepositoryProfileData
) : ViewModel() {
    val statusProfile = MutableStateFlow<UsersDb?>(null)
    val user = repositoryUser.userID

    fun create() {
        repositoryUser.getPreference()
        viewModelScope.launch(Dispatchers.IO) {
            if (repositoryUser.checkStatus(repositoryUser.userID)?.isCreator == true) {
                router.newRootScreen(Screens.routeToCreatorList())
            }else{
                router.newRootScreen(Screens.routeToListFragment())
            }
        }
    }

    fun checkAccount(){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.checkAccountForStatus(repositoryUser.userID)?.filterNotNull()?.collect{
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