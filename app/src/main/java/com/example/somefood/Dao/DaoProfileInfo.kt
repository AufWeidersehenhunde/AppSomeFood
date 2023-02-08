package com.example.appsomefood.Dao

import androidx.room.Dao
import androidx.room.Query
import com.example.appsomefood.DBandProvider.UsersDb
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoProfileInfo {

    @Query("UPDATE users SET name =:name WHERE uuid=:uuid")
    suspend fun updateName(name: String, uuid: String)

    @Query("UPDATE users SET description =:des WHERE uuid=:uuid")
    suspend fun updateDescription(des: String, uuid: String)

    @Query("UPDATE users SET address =:address WHERE uuid=:uuid")
    suspend fun updateAddress(address: String, uuid: String)

    @Query("UPDATE users SET isCreator =:creatorStatus WHERE uuid=:uuid")
    fun changeStatus(uuid: String, creatorStatus: Boolean?)

    @Query("SELECT* FROM users WHERE uuid=:uuid")
    fun observeAccountStatus(uuid:String): Flow<UsersDb>?

    @Query("SELECT * FROM users WHERE uuid=:idUser")
    fun takeFeedback(idUser: String): UsersDb?
}