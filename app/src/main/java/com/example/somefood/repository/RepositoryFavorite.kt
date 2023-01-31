package com.example.appsomefood.repository

import com.example.appsomefood.DBandProvider.FavoriteFoods
import com.example.appsomefood.Dao.DaoFavorite

class RepositoryFavorite(
    private val favorite: DaoFavorite
) {

    fun addFoodToFavorite(idFood: String, idUser: String) {
        val model = FavoriteFoods(idFood = idFood, idUser = idUser)
        favorite.addFoodToFavorite(model)
    }

    suspend fun checkFavoriteFood(uuid: String, id: String) = favorite.checkFavoriteFood(uuid, id)

    fun takeFavorite() = favorite.observeFavorite()

    suspend fun deleteFavoriteFood(model: FavoriteFoods) = favorite.deleteFavoriteFood(model)
}