package com.example.appsomefood.AuthSuccessForCreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.RepositoryOrders
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreatorListViewModel (
    private val repositoryOrders: RepositoryOrders
) : ViewModel() {
    private val _listOrdersForRecycler = MutableStateFlow<List<OrdersModel>?>(null)
    val listOrdersForRecycler:MutableStateFlow<List<OrdersModel>?> = _listOrdersForRecycler

    init {
        observeAllOrders()
    }

    private fun observeAllOrders(){
        viewModelScope.launch {
            repositoryOrders.observeAllOrdersFree(Status.FREE).collect{
                _listOrdersForRecycler.value = it
            }
        }
    }
}