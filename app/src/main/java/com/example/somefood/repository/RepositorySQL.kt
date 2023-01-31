package com.example.appsomefood.repository

import android.content.Context
import android.net.Uri
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Dao.DaoUser
import java.io.File
import java.util.*

class RepositorySQL (
    private val User: DaoUser,
    private val context: Context,
    preference:Reference
){

    val pref = preference.getValue("pref").toString()

    fun registerUser(usersDb:UsersDb) = User.registerUser(usersDb)

    suspend fun checkLogin(log:String) = User.checkLogin(log)

    suspend fun checkAccount(log: String, pass:String) = User.checkAccount(log, pass)

    fun checkStatus(UUID:String) = User.checkStatus(UUID)

    fun takeProfileInfo(uuid: String) = User.takeProfileInfo(uuid)

    suspend fun setPhoto(userID: String, profilePhoto: String) {
        User.setPhoto(userID, profilePhoto)
    }

    private suspend fun getUserPhoto(){
        User.getUserPhoto(pref)
    }

    suspend fun savePhoto(newUri: Uri?): String {
        val byteArray = newUri?.let { context.contentResolver?.openInputStream(it)?.readBytes() }
        val folder = File(context.filesDir, "Avatars")
        folder.mkdirs()
        File(getUserPhoto().toString()).delete()
        val file = File(folder, "${UUID.randomUUID()}")
        if (byteArray != null) {
            file.writeBytes(byteArray)
        }
        return Uri.parse(file.absolutePath).toString()
    }

}