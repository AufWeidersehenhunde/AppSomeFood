package com.example.appsomefood.AuthSuccessForCreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryOrders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreatorListViewModel (
    private val repositoryOrders: RepositoryOrders,
    preference:Reference
) : ViewModel() {
    private val _listOrdersForRecycler = MutableStateFlow<List<OrdersModel>?>(null)
    val listOrdersForRecycler:MutableStateFlow<List<OrdersModel>?> = _listOrdersForRecycler
    val user = preference.getValue("pref").toString()

    init {
        takeAllOrders()
    }


    private fun takeAllOrders(){
        viewModelScope.launch {
            repositoryOrders.takeAllOrdersFree(Status.FREE).collect{
                _listOrdersForRecycler.value = it
            }
        }
    }
}