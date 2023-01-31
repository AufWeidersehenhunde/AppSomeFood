package com.example.appsomefood.DBandProvider

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.appsomefood.Orders.Status
import java.util.*

@Entity(tableName = "orders")
data class Orders(
    @PrimaryKey var number:String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "idFood") val idFood:String,
    @ColumnInfo(name = "idUser") val idUser:String,
    @ColumnInfo(name = "time") val time:String,
    @ColumnInfo(name = "volume") val volume:Int?,
    @ColumnInfo(name = "status") val status: Status = Status.FREE,
    @ColumnInfo(name = "nameCreator") val nameCreator:String? = null,
    @ColumnInfo(name = "idCreator") val idCreator:String? = null,
    @ColumnInfo (name= "textForCreator") val textForCreator:String? = "",
    @ColumnInfo (name = "textForClient") val textForClient:String? = "",
    @ColumnInfo (name = "markForCreator") val markForCreator:Double?=null,
    @ColumnInfo (name = "markForClient") val markForClient:Double?=null,
)
