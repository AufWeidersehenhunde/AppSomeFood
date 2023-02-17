package com.example.somefood.AllScreens.ProfileFragment.DataProfile

import com.example.appsomefood.DBandProvider.UsersDb

data class LastFeedback(
    val profile: UsersDb?,
    val text: String?,
    val markFeedback: Double?,
    val markByFeedback: Double?
)