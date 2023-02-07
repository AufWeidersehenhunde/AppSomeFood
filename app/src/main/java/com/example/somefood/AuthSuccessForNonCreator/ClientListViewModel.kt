package com.example.appsomefood.AuthSuccessForNonCreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.repository.RepositoryFavorite
import com.example.appsomefood.repository.RepositoryFood
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ClientListViewModel(
    private val repositoryFavorite: RepositoryFavorite,
    private val repositoryFood: RepositoryFood
) : ViewModel() {
    private val _listFoods = MutableStateFlow<List<FoodDb>>(emptyList())
    val listFoods: MutableStateFlow<List<FoodDb>> = _listFoods

    init {
        observe()
    }

    fun putFoodToFavorite(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFavorite.checkFavoriteFood(uuid)
        }
    }

    private fun observe() {
        viewModelScope.launch {
            repositoryFood.observeAllFood().collect {
                _listFoods.value = it
            }
        }
    }
}