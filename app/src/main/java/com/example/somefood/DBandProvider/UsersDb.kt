package com.example.appsomefood.DBandProvider

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "users")
data class UsersDb(
    @PrimaryKey var uuid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "description") val description: String = "",
    @ColumnInfo(name = "name") val name: String = "User",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "icon") val icon: String? = null,
    @ColumnInfo(name = "isCreator") val isCreator: Boolean? = null,
    @ColumnInfo(name = "ordersOrdered") val ordersOrdered: Int? = null,
    @ColumnInfo(name = "ordersDone") val ordersDone: Int? = null,
    )

