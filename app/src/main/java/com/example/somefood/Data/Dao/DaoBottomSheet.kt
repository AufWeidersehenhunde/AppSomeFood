package com.example.appsomefood.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.DBandProvider.Orders
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoBottomSheet {

    @Query("SELECT*FROM food WHERE idFood=:it")
    suspend fun observeFoodForId(it: String): FoodDb

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFoodToOrder(model: Orders)
}