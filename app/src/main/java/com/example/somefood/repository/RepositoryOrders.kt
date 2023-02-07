package com.example.appsomefood.repository

import com.example.appsomefood.Dao.DaoOrders
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import kotlinx.coroutines.flow.Flow

class RepositoryOrders(
    private val order: DaoOrders
) {
    fun observeForRV(uuid: String, status: Status): Flow<List<OrdersModel>?> {
        return order.observeOrdersForClient(uuid, status)
    }

    fun observeForRVLastest(uuid: String, status: Status): Flow<List<OrdersModel>?> {
        return order.observeOrdersForLastest(uuid, status)
    }

    fun observeRatingForFeedbackByCreator(uuid: String) = order.observeRatingForCreator(uuid)

    fun observeRatingForFeedbackByClient(uuid: String) = order.observeRatingForClient(uuid)

    suspend fun delOrder(id: String) = order.delOrder(id)

    fun observeAllOrders(uuid: String, status: Status) = order.observeOrders(uuid,status)

    fun observeAllOrdersFree(statusFree: Status) = order.observeAllOrders(statusFree)

    fun updateFeedbackByClient(idOrder: String, idClient: String, textForCreator: String, markForCreator: Double) = order.updateFeedbackByClient(idOrder, idClient,textForCreator,markForCreator)

    fun updateFeedbackByCreator(idOrder: String, idCreator: String, textForClient: String, markForClient: Double) = order.updateFeedbackByCreator(idOrder, idCreator,textForClient,markForClient)

    fun observeFeedbackForClient(idClient: String) = order.observeFeedbackForClient(idClient)

    fun observeFeedbackForCreator(idCreator: String) = order.observeFeedbackForCreator(idCreator)

    fun observeInWorkAndDone(user: String, statusFirst: Status, statusSecond: Status) = order.observeOrdersForCreatorWork(user,statusFirst, statusSecond)

    fun updateOrder(idOrder: String, creator: String, uuid: String, status: Status) = order.updateOrder(idOrder, creator,uuid, status)

    fun updateOrderDoneForCreator(number: String, creator: String, done: Status, id: String) = order.updateOrderDoneForCreator(number, creator,done, id)

    fun updateOrderArchive(orderId: String, status: Status) = order.updateOrderArchive(orderId, status)

    suspend fun takeOrdersDone(idCreator: String, status: Status) = order.takeOrdersDone(idCreator, status ).size

    suspend fun takeOrdersOrdered(idUser: String, status: Status) = order.takeOrdersOrdered(idUser, status ).size


}