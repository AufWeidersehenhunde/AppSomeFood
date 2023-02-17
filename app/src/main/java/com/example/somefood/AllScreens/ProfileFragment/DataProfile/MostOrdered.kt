package com.example.somefood.AllScreens.ProfileFragment.DataProfile

import com.example.appsomefood.DBandProvider.FoodDb

data class MostOrdered(
    val food: FoodDb?,
    val count: String?
)