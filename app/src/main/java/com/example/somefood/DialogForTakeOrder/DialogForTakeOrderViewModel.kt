package com.example.appsomefood.DialogForTakeOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryOrders
import com.example.appsomefood.repository.RepositorySQL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DialogForTakeOrderViewModel (
    private val repositoryOrders: RepositoryOrders,
    private val repositorySQL: RepositorySQL,
    private val preference:Reference
) : ViewModel() {
    val user = preference.getValue("pref").toString()

    fun takeOrder(idOrder: String ) {
        viewModelScope.launch(Dispatchers.IO) {
            repositorySQL.takeProfileInfo(user).collect{
                repositoryOrders.takeOrder(idOrder, it.name, it.uuid, Status.WORK)
            }
        }
    }
}