package com.example.appsomefood.DialogForTakeOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.RepositoryOrders
import com.example.appsomefood.repository.RepositoryUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DialogForTakeOrderViewModel (
    private val repositoryOrders: RepositoryOrders,
    private val repositoryUser: RepositoryUser,

) : ViewModel() {

    fun takeOrder(idOrder: String ) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.observeProfileInfo(repositoryUser.userID).collect{
                repositoryOrders.observeOrder(idOrder, it.name, it.uuid, Status.WORK)
            }
        }
    }
}