package com.example.appsomefood.FeedbackDialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FeedbackDialogViewModel(
    private val repositoryUser: RepositoryUser,
    private val repositoryOrders: RepositoryOrders
) : ViewModel() {
    private val _profile = MutableStateFlow<UsersDb?>(null)
    var profile: MutableStateFlow<UsersDb?> = _profile


    fun updateFeedback(id: String, text: String, mark: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            if (_profile.value?.isCreator == false) {
                repositoryOrders.updateFeedbackByClient(id, repositoryUser.userID, text, mark)
            } else {
                repositoryOrders.updateFeedbackByCreator(id, repositoryUser.userID, text, mark)
            }
        }
    }


    fun checkStatus() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.observeProfileInfo(repositoryUser.userID).collect {
                _profile.value = it
            }
        }
    }


}