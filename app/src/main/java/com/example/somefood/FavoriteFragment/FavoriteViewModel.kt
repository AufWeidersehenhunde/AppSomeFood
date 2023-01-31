package com.example.appsomefood.FavoriteFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.repository.Reference
import com.example.appsomefood.repository.RepositoryFavorite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val repositoryFavorite: RepositoryFavorite,
    preference:Reference
) : ViewModel() {
    val user = preference.getValue("pref").toString()
    private val _listFoods = MutableStateFlow<List<FavoriteModel>?>(emptyList())
    val listFoods: MutableStateFlow<List<FavoriteModel>?> = _listFoods

    init {
        takeFavorite()
    }

    fun delFoodInFavorite(uuid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val check = repositoryFavorite.checkFavoriteFood(uuid, user)
            if (check != null) {
                repositoryFavorite.deleteFavoriteFood(check)
            }
        }
    }


    private fun takeFavorite() {
        viewModelScope.launch {
            repositoryFavorite.takeFavorite().collect {
                _listFoods.value = it
            }
        }
    }
}


