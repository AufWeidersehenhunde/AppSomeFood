package com.example.appsomefood.repository

import com.example.appsomefood.DBandProvider.Orders
import com.example.appsomefood.Dao.DaoBottomSheet

class RepositoryBottomSheet(
    private val bSheet: DaoBottomSheet
) {
    fun takeFoodForSheet(it: String) = bSheet.takeFoodForSheet(it)

    fun addFoodToOrder(uuid: String, id: String, time: String, volume: Int) {
        val model = Orders(idFood = uuid, idUser = id, time = time, volume = volume)
        bSheet.addFoodToOrder(model)
    }
}