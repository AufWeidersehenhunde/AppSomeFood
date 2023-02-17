package com.example.appsomefood.FavoriteFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.repository.RepositoryFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repositoryFavorite: RepositoryFavorite
) : ViewModel() {
    private val _listFoods = MutableStateFlow<List<FavoriteModel>?>(null)
    val listFoods: MutableStateFlow<List<FavoriteModel>?> = _listFoods

    init {
        observeFavorite()
    }

    fun delFoodInFavorite(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFavorite.checkFavoriteFood(uuid)
        }
    }

    private fun observeFavorite() {
        viewModelScope.launch {
            repositoryFavorite.observeFavorite().collect {
                _listFoods.value = it
            }
        }
    }
}


