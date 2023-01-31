package com.example.appsomefood.repository

import com.example.appsomefood.Dao.DaoProfileInfo

class RepositoryProfileData (
    private val data: DaoProfileInfo
){

    suspend fun updateName(name:String, uuid: String) = data.updateName(name, uuid)

    suspend fun updateDescription(des:String, uuid: String) = data.updateDescription(des, uuid)

    suspend fun updateAddress(address:String, uuid: String) = data.updateAddress(address, uuid)

    fun setStatus(login: String, creator: Boolean?) = data.setStatus(login, creator)

    fun changeStatus(uuid:String, creatorStatus: Boolean) = data.changeStatus(uuid, creatorStatus)

    fun checkAccountForStatus(it:String) = data.checkAccountForStatus(it)

    fun takeFeedback(idUser: String) = data.takeFeedback(idUser)


}