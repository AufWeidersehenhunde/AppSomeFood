package com.example.appsomefood.DialogForTakeOrder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.RepositoryFood
import com.example.appsomefood.repository.RepositoryOrders
import com.example.appsomefood.repository.RepositoryUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DialogForTakeOrderViewModel(
    private val repositoryOrders: RepositoryOrders,
    private val repositoryUser: RepositoryUser,
    private val repositoryFood: RepositoryFood
) : ViewModel() {
    val item = MutableStateFlow<FoodDb?>(null)

    fun updateOrder(idOrder: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val model = repositoryUser.getProfileInfo(repositoryUser.userID)
            if (model != null) {
                repositoryOrders.updateOrder(idOrder, model.name, model.uuid, Status.WORK)
            }
        }
    }

    fun takeFood(id:String) {
        viewModelScope.launch(Dispatchers.IO) {
            item.value = repositoryFood.observeFoodForMustOrder(id)
        }
    }
}