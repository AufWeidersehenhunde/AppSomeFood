package com.example.appsomefood.AuthFragment

import android.annotation.SuppressLint
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
import kotlinx.coroutines.launch

class AuthViewModel(
    private val router: Router,
    private val repositorySQL: RepositorySQL,
    private val repositoryProfileData: RepositoryProfileData,
    private val preference:Reference
) : ViewModel() {
    private val _auth = MutableStateFlow<Boolean?>(null)
    val auth: MutableStateFlow<Boolean?> = _auth

    fun routeToBack() {
        router.backTo(Screens.routeToHomeFragment())
    }
    
    fun authentication(model: UsersDb) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.setStatus(model.login, model.isCreator)
            val check = repositorySQL.checkAccount(model.login, model.password)
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