package com.example.appsomefood.AuthSuccessForNonCreator

import android.util.Log
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
        observe()
    }

    fun putFoodToFavorite(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFavorite.checkFavoriteFood(uuid)
        }
    }

    private fun observe() {
        viewModelScope.launch {
            repositoryFood.observeFavoriteFoods().collect {
                _foods.value = it
            }
        }
    }
}



