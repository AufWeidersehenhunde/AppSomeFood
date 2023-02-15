package com.example.appsomefood.RegistrationFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.somefood.Navigation.Screens
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryUser
import com.example.somefood.Utils.EnumAndSealed.ToastAuth
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class RegistrationViewModel(
    private val router: Router,
    private val repositoryUser: RepositoryUser,
    private val preference: Reference
) : ViewModel() {
    var toast = MutableStateFlow<ToastAuth?>(null)
    private var _regBoolean = MutableStateFlow(false)
    var regBoolean: MutableStateFlow<Boolean> = _regBoolean

    fun goToBack() {
        router.navigateTo(Screens.routeToHomeFragment())
    }


    private fun register(log: String, pass: String, status: Boolean) {
        _regBoolean.value = false
        val model = UsersDb(
            login = log,
            password = pass,
            isCreator = status
        )
        viewModelScope.launch(Dispatchers.IO) {
            if (repositoryUser.checkLogin(model.login) == model.login) {
                _regBoolean.value = true
            } else {
                repositoryUser.registerUser(model)
                preference.save("pref", model.uuid)
                router.newRootScreen(Screens.routeToFragmentContainer())
            }
        }
    }

    fun checkInput(login: String, password: String, secondPassword: String, status: Boolean) {
        if (password.isEmpty() && secondPassword.isEmpty()) {
            toast.value = ToastAuth.PASS
        } else if (login.isEmpty()) {
            toast.value = ToastAuth.LOGIN

        } else if (!isEmailValid(login)) {
            toast.value = ToastAuth.LOGININVALID
        } else if(password!=secondPassword){
            toast.value = ToastAuth.PASSINVALID
        } else {
            register(login, password, status)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        val matcher = pattern.matcher(email);
        return matcher.matches()
    }
}