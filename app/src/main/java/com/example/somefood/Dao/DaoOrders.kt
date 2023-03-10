package com.example.appsomefood.Dao

import androidx.room.Dao
import androidx.room.Query
import com.example.appsomefood.DBandProvider.Orders
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoOrders {

    @Query("SELECT*FROM orders")
    fun takeOrders(): Flow<List<Orders>?>

    @Query("SELECT * FROM orders WHERE number=:id")
    fun tak(id:String):Flow<Orders?>

    @Query("SELECT * FROM orders WHERE number=:it")
    fun check(it:String): Flow<Orders?>

    @Query("SELECT * FROM orders WHERE idCreator=:uuid")
    fun takeRatingForFeedbackByCreator(uuid:String):Flow<List<Orders>?>

    @Query("SELECT * FROM orders WHERE idUser=:uuid")
    fun takeRatingForFeedbackByClient(uuid:String):Flow<List<Orders>?>

    @Query("UPDATE orders SET idUser=:idClient, textForCreator=:textForCreator, markForCreator=:markForCreator WHERE number=:id")
    fun insertFeedbackByClient(id:String, idClient:String, textForCreator:String, markForCreator:Double)

    @Query("UPDATE orders SET idCreator=:idCreator, textForClient=:textForClient, markForClient=:markForClient WHERE number=:id")
    fun insertFeedbackByCreator(id:String, idCreator:String, textForClient:String, markForClient:Double)

    @Query("SELECT * FROM orders WHERE idUser=:it")
    fun takeFeedbackForClient(it:String):Flow<Orders?>

    @Query("SELECT * FROM orders WHERE idCreator=:it")
    fun takeFeedbackForCreator(it:String):Flow<Orders?>

    @Query("SELECT * FROM orders WHERE idUser=:it ")
    fun takeMarksForClient(it:String):Flow<List<Orders>?>

    @Query("SELECT * FROM orders WHERE idCreator=:it ")
    fun takeMarksForCreator(it:String):Flow<List<Orders>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.status =:FREE and orders.idUser=:uuid")
    fun leftJoinTablesForCreator(uuid:String, FREE: Status): Flow<List<OrdersModel>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.status =:FREE")
    fun leftJoinTablesAll( FREE: Status): Flow<List<OrdersModel>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.idUser =:id and orders.status!=:status")
    fun leftJoinTablesForClient(id:String, status: Status): Flow<List<OrdersModel>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.idUser =:id and orders.status=:status")
    fun leftJoinTablesForLastest(id:String, status: Status): Flow<List<OrdersModel>?>

    @Query("SELECT * FROM orders LEFT JOIN food ON orders.idFood = food.idFood WHERE orders.status=:it or orders.status=:it2 and orders.idCreator=:user")
    fun leftJoinTablesForOrdersCreator(user:String, it:Status,it2:Status):Flow<List<OrdersModel>?>

    @Query("DELETE FROM orders WHERE number =:id")
    suspend fun delOrder(id:String)

    @Query("UPDATE orders SET status=:status, nameCreator=:name, idCreator=:uuid WHERE number=:idOrder")
    fun takeOrder(idOrder: String, name:String, uuid:String, status: Status)

    @Query("UPDATE orders SET status=:done, nameCreator=:name, idCreator=:id WHERE number=:number")
    fun orderDoneForCreator(number: String, name:String, done: Status, id:String)

    @Query("UPDATE orders SET status=:status WHERE number=:number")
    fun orderArchive(number: String, status: Status)

    @Query("SELECT * FROM orders WHERE idCreator=:id and status=:status")
    suspend fun takeOrdersDone(id:String, status: Status):List<Orders>

    @Query("SELECT * FROM orders WHERE idUser=:id and status=:status")
    suspend fun takeOrdersOrdered(id:String, status: Status):List<Orders>


}