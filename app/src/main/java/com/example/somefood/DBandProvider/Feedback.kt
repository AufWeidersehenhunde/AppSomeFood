package com.example.appsomefood.DBandProvider

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "feedback")
data class Feedback(
    @PrimaryKey val idFeedback:String = UUID.randomUUID().toString(),
    @ColumnInfo (name = "idOrder") val idOrder:String?,
    @ColumnInfo (name = "idCreator") val idCreator:String?,
    @ColumnInfo (name = "idClient") val idClient:String?,
    @ColumnInfo (name= "textForCreator") val textForCreator:String?,
    @ColumnInfo (name = "textForClient") val textForClient:String?,
    @ColumnInfo (name = "markForCreator") val markForCreator:Double?,
    @ColumnInfo (name = "markForClient") val markForClient:Double?,
)
