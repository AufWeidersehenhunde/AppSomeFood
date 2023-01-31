package com.example.appsomefood.RegistrationFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Screens
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositorySQL
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val router: Router,
    private val repositorySQL: RepositorySQL,
    private val preference: Reference
) : ViewModel() {

    private var _regBoolean = MutableStateFlow(false)
    var regBoolean: MutableStateFlow<Boolean> = _regBoolean

    fun goToBack() {
        router.navigateTo(Screens.routeToHomeFragment())
    }


    fun register(model: UsersDb) {
        _regBoolean.value = false
        viewModelScope.launch(Dispatchers.IO) {
            if (repositorySQL.checkLogin(model.login) == model.login) {
                _regBoolean.value = true
            } else {
                repositorySQL.registerUser(model)
                preference.save("pref", model.uuid)
                router.newRootScreen(Screens.routeToFragmentContainer() )
            }
        }
    }
}