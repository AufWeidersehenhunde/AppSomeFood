package com.example.appsomefood.Dao

import androidx.room.Dao
import androidx.room.Query
import com.example.appsomefood.DBandProvider.Orders
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoOrders {

    @Query("SELECT * FROM orders WHERE idCreator=:uuid")
    fun observeRatingForCreator(uuid:String): Flow<List<Orders>?>

    @Query("SELECT * FROM orders WHERE idUser=:uuid")
    fun observeRatingForClient(uuid:String): Flow<List<Orders>?>

    @Query("UPDATE orders SET idUser=:idClient, textForCreator=:textForCreator, markForCreator=:markForCreator WHERE number=:id")
    fun updateFeedbackByClient(id: String, idClient: String, textForCreator: String, markForCreator: Double)

    @Query("UPDATE orders SET idCreator=:idCreator, textForClient=:textForClient, markForClient=:markForClient WHERE number=:id")
    fun updateFeedbackByCreator(id: String, idCreator: String, textForClient: String, markForClient: Double)

    @Query("SELECT * FROM orders WHERE idUser=:it")
    fun observeFeedbackForClient(it: String): Flow<List<Orders>?>

    @Query("SELECT * FROM orders WHERE idCreator=:it")
    fun observeFeedbackForCreator(it: String): Flow<List<Orders>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.status =:status and orders.idUser=:uuid")
    fun observeOrders(uuid: String, status: Status): Flow<List<OrdersModel>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.status =:statusFree")
    fun observeAllOrders(statusFree: Status): Flow<List<OrdersModel>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.idUser =:id and orders.status!=:status")
    fun observeOrdersForClient(id: String, status: Status): Flow<List<OrdersModel>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.idUser =:id and orders.status=:status")
    fun observeOrdersForLastest(id: String, status: Status): Flow<List<OrdersModel>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.status=:status or orders.status=:secondStatus and orders.idCreator=:user")
    fun observeOrdersForCreatorWork(user: String, status: Status, secondStatus: Status): Flow<List<OrdersModel>?>

    @Query("DELETE FROM orders WHERE number =:id")
    suspend fun delOrder(id: String)

    @Query("UPDATE orders SET status=:status, nameCreator=:name, idCreator=:uuid WHERE number=:idOrder")
    fun updateOrder(idOrder: String, name: String, uuid: String, status: Status)

    @Query("UPDATE orders SET status=:done, nameCreator=:name, idCreator=:id WHERE number=:number")
    fun updateOrderDoneForCreator(number: String, name: String, done: Status, id: String)

    @Query("UPDATE orders SET status=:status WHERE number=:number")
    fun updateOrderArchive(number: String, status: Status)

    @Query("SELECT * FROM orders WHERE idCreator=:id and status=:status")
    suspend fun takeOrdersDone(id: String, status: Status): List<Orders>

    @Query("SELECT * FROM orders WHERE idUser=:id and status=:status")
    suspend fun takeOrdersOrdered(id:String, status: Status): List<Orders>
}