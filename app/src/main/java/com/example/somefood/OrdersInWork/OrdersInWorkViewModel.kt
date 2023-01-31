package com.example.appsomefood.OrdersInWork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryOrders
import com.example.appsomefood.repository.RepositorySQL
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrdersInWorkViewModel (
    private val repositoryOrders: RepositoryOrders,
    private val repositorySQL: RepositorySQL,
    preference:Reference
) : ViewModel() {
    val user = preference.getValue("pref").toString()
    private val _listOrdersForRecycler = MutableStateFlow<List<OrdersModel>?>(null)
    val listOrdersForRecycler : MutableStateFlow<List<OrdersModel>?> = _listOrdersForRecycler

    init{
        takeAllOrders()
    }


    private fun takeAllOrders(){
        viewModelScope.launch(Dispatchers.IO) {
            repositoryOrders.takeInWorkAndDone(user,Status.WORK, Status.DONE).collect{
                _listOrdersForRecycler.value = it
            }
        }
    }

    fun orderDone(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositorySQL.takeProfileInfo(user).collect {
                repositoryOrders.orderDoneForCreator(number, it.name, Status.DONE, it.uuid)
            }
        }
    }
}