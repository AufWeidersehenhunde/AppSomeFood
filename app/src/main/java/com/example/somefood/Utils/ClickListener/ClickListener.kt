package com.example.somefood.Utils.ClickListener

import com.example.appsomefood.Orders.OrdersModel


sealed class ClickListener

class TakeOrder(val item: OrdersModel) : ClickListener()

class AddToFavorite(val idFood: String) : ClickListener()

class AddToOrder(val idFood: String) : ClickListener()

class DeleteFavorite(val idFood: String) : ClickListener()

class DeleteOrder(val idOrder: String) : ClickListener()

class AcceptOrder(val order: OrdersModel) : ClickListener()

class Dialog(val idOrder: String, val userId: String) : ClickListener()

class Order(val order: OrdersModel) : ClickListener()

class DoneOrder(val order: OrdersModel) : ClickListener()