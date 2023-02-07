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
import java.util.regex.Pattern

class AuthViewModel(
    private val router: Router,
    private val repositoryUser: RepositoryUser,
    private val repositoryProfileData: RepositoryProfileData,
    private val preference: Reference
) : ViewModel() {
    private val _auth = MutableStateFlow<Boolean?>(null)
    val auth: MutableStateFlow<Boolean?> = _auth
    var toast = MutableStateFlow<toastAuth?>(null)

    fun routeToBack() {
        router.backTo(Screens.routeToHomeFragment())
    }

    private fun authentication(log: String, pass: String, status: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = UsersDb(
                login = log,
                password = pass,
                isCreator = status
            )
            val check = repositoryUser.checkAccount(model.login, model.password)
            if (check != null) {
                repositoryProfileData.changeStatus(check.uuid, model.isCreator)
            }
            if (check != null) {
                _auth.value = true
                preference.save("pref", check.uuid)
                router.newRootScreen(Screens.routeToFragmentContainer())
            } else {
                _auth.value = false
            }
        }
    }

    fun checkInput(login: String, password: String, status: Boolean) {
        if (password.isEmpty()) {
            toast.value = toastAuth.PASS

        } else if (login.isEmpty()) {
            toast.value = toastAuth.LOGIN

        } else if (!isEmailValid(login)) {
            toast.value = toastAuth.LOGININVALID

        } else {
            authentication(login, password, status)
        }
    }

    private fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        val matcher = pattern.matcher(email);
        return matcher.matches()
    }
}

enum class toastAuth {
    PASS,
    LOGIN,
    LOGININVALID,
    PASSINVALID
}