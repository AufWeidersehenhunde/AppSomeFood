package com.example.appsomefood.repository

import com.example.appsomefood.Dao.DaoFood

class RepositoryFood(
    private val food: DaoFood
) {

    fun observeAllFood() = food.observeFood()
    suspend fun observeFoodForMustOrder(name: String) = food.takeFoodForMustOrder(name)
}