package com.example.appsomefood.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.somefood.Data.DBandProvider.FavoriteFoods
import com.example.appsomefood.FavoriteFragment.FavoriteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoFavorite {
    @Insert
    fun addFoodToFavorite(model: FavoriteFoods)

    @Query("SELECT * FROM food LEFT JOIN favorites ON food.idFood = favorites.idFoodFavorite WHERE food.idFood = favorites.idFoodFavorite")
    fun observeFavorite(): Flow<List<FavoriteModel>?>

    @Query("SELECT * FROM favorites WHERE idFoodFavorite =:uuid AND idUser=:id")
    fun checkFavoriteFood(uuid: String, id: String): FavoriteFoods?

    @Delete
    suspend fun deleteFavoriteFood(model: FavoriteFoods)
}