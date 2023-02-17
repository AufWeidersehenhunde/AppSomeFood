package com.example.appsomefood.Dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.repository.RepositoryOrders
import kotlinx.coroutines.launch

class DialogViewModel(
    private val repositoryOrders: RepositoryOrders
) : ViewModel() {

    fun delOrder(id: String) {
        viewModelScope.launch {
            repositoryOrders.delOrder(id)
        }
    }
}