package com.example.appsomefood.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.appsomefood.DBandProvider.*
import kotlinx.coroutines.flow.Flow


@Dao
interface DaoUser {

    @Insert
    fun registerUser(usersDb: UsersDb)

    @Query("SELECT login FROM users WHERE login =:log")
    suspend fun checkLogin(log: String): String

    @Query("SELECT* FROM users WHERE login =:log AND password=:pass")
    suspend fun checkAccount(log: String, pass: String): UsersDb?

    @Query("SELECT*FROM users WHERE uuid=:UUID")
    fun checkStatus(UUID: String): UsersDb?

    @Query("SELECT*FROM users WHERE uuid=:uuid")
    fun takeProfileInfo(uuid: String): Flow<UsersDb>

    @Query("UPDATE users SET icon = :newPhoto WHERE uuid = :userId")
    suspend fun setPhoto(userId: String, newPhoto: String)

    @Query("SELECT icon FROM users WHERE uuid LIKE :userID")
    suspend fun getUserPhoto(userID: String): String


}