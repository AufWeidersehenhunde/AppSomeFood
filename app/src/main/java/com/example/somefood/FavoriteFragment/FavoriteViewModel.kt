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
    private val _listFoods = MutableStateFlow<List<FavoriteModel>?>(emptyList())
    val listFoods: MutableStateFlow<List<FavoriteModel>?> = _listFoods

    init {
        takeFavorite()
    }

    fun delFoodInFavorite(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryFavorite.checkFavoriteFood(uuid)
        }
    }


    private fun takeFavorite() {
        viewModelScope.launch {
            repositoryFavorite.observeFavorite().collect {
                _listFoods.value = it
            }
        }
    }
}


