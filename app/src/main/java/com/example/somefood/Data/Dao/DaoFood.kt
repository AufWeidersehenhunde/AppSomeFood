package com.example.appsomefood.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.somefood.AllScreens.ClientList.Foods
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoFood {
    @Query("SELECT*FROM food")
    fun observeFood(): Flow<List<FoodDb>>

    @Insert
    fun insertFoods(list: List<FoodDb>)

    @Query("SELECT*FROM food WHERE name=:it")
    fun takeFoodForMustOrder(it: String): FoodDb?

    @Query("SELECT * FROM food LEFT JOIN favorites ON food.idFood = favorites.idFoodFavorite and favorites.idUser =:id")
    fun observeFavoriteFoods(id:String): Flow<List<Foods>?>
}