package com.example.appsomefood.repository

import com.example.appsomefood.Dao.DaoOrders
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import kotlinx.coroutines.flow.Flow

class RepositoryOrders(
    private val order:DaoOrders
) {
    fun observeForRV(uuid: String, status: Status): Flow<List<OrdersModel>?> {
        return order.observeOrdersForClient(uuid, status)
    }

    fun observeForRVLastest(uuid: String, status: Status): Flow<List<OrdersModel>?> {
        return order.observeOrdersForLastest(uuid, status)
    }

    fun observeRatingForFeedbackByCreator(uuid:String) = order.observeRatingForCreator(uuid)

    fun observeRatingForFeedbackByClient(uuid:String) = order.observeRatingForClient(uuid)

    suspend fun delOrder(id: String) = order.delOrder(id)

    fun observeAllOrders(uuid: String, status: Status) = order.observeOrders(uuid,status)

    fun observeAllOrdersFree(statusFree: Status) = order.observeAllOrders(statusFree)

    fun insertFeedbackByClient(idOrder:String,idClient:String, textForCreator:String, markForCreator:Double) = order.insertFeedbackByClient(idOrder, idClient,textForCreator,markForCreator)

    fun insertFeedbackByCreator(idOrder:String,idCreator:String, textForClient:String, markForClient:Double) = order.insertFeedbackByCreator(idOrder, idCreator,textForClient,markForClient)

    fun observeFeedbackForClient(idClient:String)  = order.observeFeedbackForClient(idClient)

    fun observeFeedbackForCreator(idCreator:String)  = order.observeFeedbackForCreator(idCreator)

    fun observeInWorkAndDone(user:String, statusFirst:Status, statusSecond:Status) = order.observeOrdersForCreatorWork(user,statusFirst, statusSecond)

    fun observeOrder(idOrder: String, creator:String, uuid:String, status: Status) = order.observeOrder(idOrder, creator,uuid, status)

    fun orderDoneForCreator(number: String, creator:String, done: Status, id: String) = order.orderDoneForCreator(number, creator,done, id)

    fun orderArchive(orderId:String, status: Status) = order.orderArchive(orderId, status)

    suspend fun takeOrdersDone(idCreator: String, status: Status)  = order.takeOrdersDone(idCreator, status ).size

    suspend fun takeOrdersOrdered(idUser: String, status: Status)  = order.takeOrdersOrdered(idUser, status ).size


}