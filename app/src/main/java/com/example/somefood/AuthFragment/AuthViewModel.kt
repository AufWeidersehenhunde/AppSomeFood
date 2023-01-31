package com.example.appsomefood.AuthFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Screens
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryProfileData
import com.example.appsomefood.repository.RepositoryUser
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val router: Router,
    private val repositoryUser: RepositoryUser,
    private val repositoryProfileData: RepositoryProfileData,
    private val preference: Reference
) : ViewModel() {
    private val _auth = MutableStateFlow<Boolean?>(null)
    val auth: MutableStateFlow<Boolean?> = _auth

    fun routeToBack() {
        router.backTo(Screens.routeToHomeFragment())
    }

    fun authentication(log:String, pass:String, status:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = UsersDb(
                login = log,
                password = pass,
                isCreator = status
            )
            repositoryProfileData.changeStatus(model.uuid, model.isCreator)
            val check = repositoryUser.checkAccount(model.login, model.password)
            if (check != null )  {
                _auth.value = true
                    preference.save("pref", check.uuid)
                    router.newRootScreen(Screens.routeToFragmentContainer())
                }

            else {
                _auth.value = false
            }
        }
    }
}