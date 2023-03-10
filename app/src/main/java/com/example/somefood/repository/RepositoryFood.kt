package com.example.appsomefood.repository

import com.example.appsomefood.Dao.DaoFood

class RepositoryFood(
    private val food: DaoFood
) {

    fun takeIt() = food.takeIt()
    suspend fun takeFoodForMustOrder(it:String) = food.takeFoodForMustOrder(it)
}