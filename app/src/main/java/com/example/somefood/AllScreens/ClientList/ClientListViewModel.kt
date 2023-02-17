package com.example.appsomefood.AuthSuccessForNonCreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.repository.RepositoryFavorite
import com.example.appsomefood.repository.RepositoryFood
import com.example.somefood.AllScreens.ClientList.Foods
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ClientListViewModel(
    private val repositoryFavorite: RepositoryFavorite,
    private val repositoryFood: RepositoryFood
) : ViewModel() {
    private val _foods = MutableStateFlow<List<Foods>?>(emptyList())
    val foods: MutableStateFlow<List<Foods>?> = _foods

    init {
        observeFood()
    }

    fun putFoodToFavorite(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFavorite.checkFavoriteFood(uuid)
        }
    }

    private fun observeFood() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFood.observeFoods().collect {
                _foods.value = it
            }
        }
    }
}



