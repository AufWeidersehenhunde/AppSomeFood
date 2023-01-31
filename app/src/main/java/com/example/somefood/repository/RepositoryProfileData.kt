package com.example.appsomefood.repository

import com.example.appsomefood.Dao.DaoProfileInfo

class RepositoryProfileData (
    private val profileData: DaoProfileInfo
){

    suspend fun updateName(name:String, uuid: String) = profileData.updateName(name, uuid)

    suspend fun updateDescription(des:String, uuid: String) = profileData.updateDescription(des, uuid)

    suspend fun updateAddress(address:String, uuid: String) = profileData.updateAddress(address, uuid)

    fun changeStatus(uuid:String, creatorStatus: Boolean?) = profileData.changeStatus(uuid, creatorStatus)

    fun checkAccountForStatus(it:String) = profileData.observeAccountStatus(it)


}