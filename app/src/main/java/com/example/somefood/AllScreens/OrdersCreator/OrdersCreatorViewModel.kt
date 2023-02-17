package com.example.appsomefood.OrdersInWork

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.RepositoryOrders
import com.example.appsomefood.repository.RepositoryUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OrdersCreatorViewModel(
    private val repositoryOrders: RepositoryOrders,
    private val repositoryUser: RepositoryUser
) : ViewModel() {
    private val _listOrdersForRecycler = MutableStateFlow<List<OrdersModel>?>(null)
    val listOrdersForRecycler: MutableStateFlow<List<OrdersModel>?> = _listOrdersForRecycler

    init {
        observeAllOrders()
    }

    private fun observeAllOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.userID.let { id ->
                repositoryOrders.observeInWorkAndDone(id, Status.WORK, Status.DONE).collect {
                    _listOrdersForRecycler.value = it
                }
            }
        }
    }

    fun orderDone(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.userID.let {
                val model = repositoryUser.getProfileInfo(it)
                if (model != null) {
                    repositoryOrders.updateOrderDoneForCreator(
                        number,
                        model.name,
                        Status.DONE,
                        model.uuid
                    )
                }
            }
        }
    }
}
