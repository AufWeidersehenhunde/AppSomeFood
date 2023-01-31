package com.example.appsomefood.FeedbackDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FeedbackDialogViewModel(
    preference: Reference,
    private val repositorySQL: RepositorySQL,
    private val repositoryOrders: RepositoryOrders
) : ViewModel() {
    val user = preference.getValue("pref").toString()
    var profile = MutableStateFlow<UsersDb?>(null)


    fun checkFeedback(id: String, text: String, mark: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            when (profile.value?.isCreator) {
                false -> {
                    repositoryOrders.insertFeedbackByClient(id, user, text, mark)
                }
                true -> {
                    repositoryOrders.insertFeedbackByCreator(id, user, text, mark)
                }
                else -> {

                }
            }
        }
    }


    fun checkStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            repositorySQL.takeProfileInfo(user).collect {
                profile.value = it
            }
        }
    }


}