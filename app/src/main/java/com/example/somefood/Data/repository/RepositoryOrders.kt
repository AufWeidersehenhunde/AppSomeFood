package com.example.appsomefood.repository

import com.example.appsomefood.DBandProvider.Orders
import com.example.appsomefood.Dao.DaoOrders
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import kotlinx.coroutines.flow.Flow

class RepositoryOrders(
    private val repositoryUser: RepositoryUser,
    private val order: DaoOrders
) {
    fun observeForRV(uuid: String, status: Status): Flow<List<OrdersModel>?> {
        return order.observeOrdersForClient(uuid, status)
    }

    fun observeForRVLastest(uuid: String, status: Status): List<OrdersModel?> {
        return order.observeOrdersForLastest(uuid, status)
    }

    fun observeRatingForFeedbackByCreator(uuid: String) = order.observeRatingForCreator(uuid)

    fun observeRatingForFeedbackByClient(uuid: String) = order.observeRatingForClient(uuid)

    suspend fun delOrder(id: String) = order.delOrder(id)

    fun observeAllOrders(uuid: String) = order.observeOrders(uuid)

    fun observeAllOrdersFree(statusFree: Status) = order.observeAllOrders(statusFree)

    fun updateFeedbackByClient(
        idOrder: String,
        idClient: String,
        textForCreator: String,
        markForCreator: Double
    ) = order.updateFeedbackByClient(idOrder, idClient, textForCreator, markForCreator)

    fun updateFeedbackByCreator(
        idOrder: String,
        idCreator: String,
        textForClient: String,
        markForClient: Double
    ) = order.updateFeedbackByCreator(idOrder, idCreator, textForClient, markForClient)

    fun observeInWorkAndDone(user: String, statusFirst: Status, statusSecond: Status) =
        order.observeOrdersForCreatorWork(user, statusFirst, statusSecond)

    fun updateOrder(idOrder: String, creator: String, uuid: String, status: Status) =
        order.updateOrder(idOrder, creator, uuid, status)

    fun updateOrderDoneForCreator(number: String, creator: String, done: Status, id: String) =
        order.updateOrderDoneForCreator(number, creator, done, id)

    fun updateOrderArchive(orderId: String, status: Status) =
        order.updateOrderArchive(orderId, status)

    suspend fun takeOrdersDone(idCreator: String) =
        order.takeOrdersDone(idCreator).size

    suspend fun takeOrdersOrdered(idUser: String) =
        order.takeOrdersOrdered(idUser).size

    fun getFeedback(userID: String, element: Boolean?):List<Orders?> {
        return if (element == true){
            order.observeFeedbackForCreator(userID)
        } else {
            order.observeFeedbackForClient(userID)
        }
    }


}