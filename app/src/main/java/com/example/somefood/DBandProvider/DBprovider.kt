package com.example.appsomefood.DBandProvider

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.appsomefood.Dao.*
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.repository.Reference

@Database(
    entities = [UsersDb::class, FoodDb::class, FavoriteFoods::class, Orders::class, Feedback::class],
    version = 1
)
abstract class DBprovider : RoomDatabase() {
    abstract fun DaoUser(): DaoUser
    abstract fun DaoOrders(): DaoOrders
    abstract fun DaoFavorite(): DaoFavorite
    abstract fun DaoBottomSheet(): DaoBottomSheet
    abstract fun DaoProfileInfo(): DaoProfileInfo
    abstract fun DaoFood(): DaoFood
}