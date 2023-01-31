package com.example.appsomefood.repository

import com.example.appsomefood.DBandProvider.FavoriteFoods
import com.example.appsomefood.Dao.DaoFavorite

class RepositoryFavorite(
    private val favorite: DaoFavorite
) {

    fun addFoodToFavorite(uuid: String, id: String) {
        val model = FavoriteFoods(idFood = uuid, idUser = id)
        favorite.addFoodToFavorite(model)
    }

    suspend fun checkFavoriteFood(uuid: String, id: String) = favorite.checkFavoriteFood(uuid, id)

    fun takeFavorite() = favorite.leftJoinForFavorite()

    suspend fun deleteFavoriteFood(model: FavoriteFoods) = favorite.deleteFavoriteFood(model)
}