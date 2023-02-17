package com.example.appsomefood.repository

import com.example.appsomefood.Dao.DaoFood

class RepositoryFood(
    private val repositoryUser: RepositoryUser,
    private val food: DaoFood
) {
    fun observeFoodForMustOrder(name: String) = food.takeFoodForMustOrder(name)

    fun observeFoods() = food.observeFoods(repositoryUser.userID)
}