package com.example.appsomefood.Orders

data class OrdersModel(
    var idFood: String,
    var number: String,
    var idUser: String?,
    var name: String,
    var image: String,
    var time: String,
    var volume: Int,
    var status: Status?,
    var nameCreator: String?
)
enum class Status{
    FREE,
    WORK,
    DONE,
    ARCHIVE,
}
