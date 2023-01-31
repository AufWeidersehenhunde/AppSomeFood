package com.example.appsomefood.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appsomefood.DBandProvider.FoodDb
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoFood {
    @Query("SELECT*FROM food")
    fun takeIt(): Flow<List<FoodDb>>

    @Insert
    fun insertFoods(list: List<FoodDb>)

    @Query("SELECT*FROM food WHERE name=:it")
     suspend fun takeFoodForMustOrder(it:String): FoodDb?
}