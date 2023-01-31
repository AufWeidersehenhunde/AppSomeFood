package com.example.appsomefood.Orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryOrders
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val repositoryOrders: RepositoryOrders,
    private val preference:Reference
) : ViewModel() {
    val user = preference.getValue("pref").toString()
    private val _listFoodsForRecycler = MutableStateFlow<List<OrdersModel>?>(null)
    val listFoodsForRecycler : MutableStateFlow<List<OrdersModel>?> = _listFoodsForRecycler

    fun takeOrders() {
        viewModelScope.launch {
            repositoryOrders.takeForRV(user, Status.ARCHIVE).collect {
                _listFoodsForRecycler.value = it
            }
        }
    }

    fun acceptOrder(order: OrdersModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (order.status == Status.DONE) {
                repositoryOrders.orderArchive(order.number, Status.ARCHIVE)
            }
        }
    }

}
