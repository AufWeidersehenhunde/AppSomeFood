package com.example.appsomefood.repository

import com.example.appsomefood.Dao.DaoOrders
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import kotlinx.coroutines.flow.Flow

class RepositoryOrders(
    private val order:DaoOrders
) {
    fun takeForRV(uuid: String, status: Status): Flow<List<OrdersModel>?> {
        return order.leftJoinTablesForClient(uuid, status)
    }
    fun tak(id: String) = order.tak(id)

    fun takeForRVLastest(uuid: String, status: Status): Flow<List<OrdersModel>?> {
        return order.leftJoinTablesForLastest(uuid, status)
    }

    fun takeRatingForFeedbackByCreator(uuid:String) = order.takeRatingForFeedbackByCreator(uuid)

    fun takeRatingForFeedbackByClient(uuid:String) = order.takeRatingForFeedbackByClient(uuid)

    suspend fun delOrder(id: String) = order.delOrder(id)

    fun takeAllOrders(uuid: String, it: Status) = order.leftJoinTablesForCreator(uuid,it)

    fun takeAllOrdersFree( it: Status) = order.leftJoinTablesAll(it)

    fun insertFeedbackByClient(id:String,idClient:String, textForCreator:String, markForCreator:Double) = order.insertFeedbackByClient(id, idClient,textForCreator,markForCreator)

    fun insertFeedbackByCreator(id:String,idCreator:String, textForClient:String, markForClient:Double) = order.insertFeedbackByCreator(id, idCreator,textForClient,markForClient)

    fun takeFeedbackForClient(it:String)  = order.takeFeedbackForClient(it)

    fun takeFeedbackForCreator(it:String)  = order.takeFeedbackForCreator(it)

    fun takeMarksForCreator(it:String) = order.takeMarksForCreator(it)

    fun takeMarksForClient(it:String) = order.takeMarksForClient(it)

    fun takeInWorkAndDone(user:String, it:Status, it2:Status) = order.leftJoinTablesForOrdersCreator(user,it, it2)

    fun takeOrder(idOrder: String, creator:String, uuid:String, status: Status) = order.takeOrder(idOrder, creator,uuid, status)

    fun orderDoneForCreator(number: String, creator:String, done: Status, id: String) = order.orderDoneForCreator(number, creator,done, id)

    fun orderArchive(orderId:String, status: Status) = order.orderArchive(orderId, status)

    suspend fun takeOrdersDone(idCreator: String, status: Status)  = order.takeOrdersDone(idCreator, status ).size

    suspend fun takeOrdersOrdered(idUser: String, status: Status)  = order.takeOrdersOrdered(idUser, status ).size


}