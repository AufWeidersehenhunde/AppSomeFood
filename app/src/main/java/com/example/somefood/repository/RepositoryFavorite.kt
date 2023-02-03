package com.example.appsomefood.repository

import com.example.appsomefood.DBandProvider.FavoriteFoods
import com.example.appsomefood.Dao.DaoFavorite

class RepositoryFavorite(
    private val favorite: DaoFavorite,
    private val repositoryUser: RepositoryUser
) {

    private fun addFoodToFavorite(idFood: String, idUser: String) {
        val model = FavoriteFoods(idFood = idFood, idUser = idUser)
        favorite.addFoodToFavorite(model)
    }

    suspend fun checkFavoriteFood(uuid: String) {
        val check = favorite.checkFavoriteFood(uuid, repositoryUser.userID)
        if (check != null) {
            deleteFavoriteFood(check)
        } else {
            addFoodToFavorite(uuid, repositoryUser.userID)
        }
    }

    fun observeFavorite() = favorite.observeFavorite()

    private suspend fun deleteFavoriteFood(model: FavoriteFoods) = favorite.deleteFavoriteFood(model)
}