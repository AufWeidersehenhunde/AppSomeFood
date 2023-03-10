package com.example.appsomefood.AuthSuccessForNonCreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryFavorite
import com.example.appsomefood.repository.RepositoryFood
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ClientListViewModel(
    private val repositoryFavorite: RepositoryFavorite,
    private val repositoryFood: RepositoryFood,
    private val preference:Reference
) : ViewModel() {
    private val _listFoods = MutableStateFlow<List<FoodDb>>(emptyList())
    val listFoods:MutableStateFlow<List<FoodDb>> = _listFoods
    val user = preference.getValue("pref").toString()


    init {
        observe()
    }


    fun putFoodToFavorite(uuid:String){
        viewModelScope.launch(Dispatchers.IO) {
            val check = repositoryFavorite.checkFavoriteFood(uuid, user)
            if (check != null) {
                repositoryFavorite.deleteFavoriteFood(check)
            } else {
                repositoryFavorite.addFoodToFavorite(uuid, user)
            }
        }
    }


    private fun observe() {
        viewModelScope.launch {
            repositoryFood.takeIt().collect {
                _listFoods.value = it
            }
        }
    }
}