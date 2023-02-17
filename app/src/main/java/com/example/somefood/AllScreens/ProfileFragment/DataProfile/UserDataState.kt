package com.example.somefood.AllScreens.ProfileFragment.DataProfile

data class UserDataState(
    val uuid: String,
    val login: String,
    val averageMark: Double?,
    val description: String = "",
    val name: String = "User",
    val address: String = "",
    val icon: String? = null,
    val isCreator: Boolean? = null,
)