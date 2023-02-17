package com.example.somefood.AllScreens.ProfileFragment.DataProfile

import com.example.appsomefood.Orders.OrdersModel

data class AllDataProfile(
    val user: UserDataState? = null,
    val order: OrdersStats? = null,
    val counter: MostOrdered? = null,
    val feedback: LastFeedback? = null,
    val listLast: List<OrdersModel?>? = null
)