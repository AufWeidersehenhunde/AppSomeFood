package com.example.appsomefood.repository

import android.content.Context
import android.net.Uri
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Dao.DaoUser
import java.io.File
import java.util.*

class RepositoryUser (
    private val user: DaoUser,
    private val context: Context,
    private val preference: Reference
){
    var userID:String = ""

    fun getPreference(){
        userID = preference.getValue("pref").toString()
    }

    fun delPref (){
        userID = preference.getValue("pref").toString()
        preference.remove("pref")}

    fun registerUser(usersDb: UsersDb) = user.registerUser(usersDb)

    suspend fun checkLogin(log: String) = user.checkLogin(log)

    suspend fun checkAccount(log: String, pass: String) = user.checkAccount(log, pass)

    fun checkStatus(UUID: String) = user.checkStatus(UUID)

    fun observeProfileInfo(uuid: String) = user.observeProfileInfo(uuid)

    suspend fun setPhoto(userID: String, profilePhoto: String) {
        user.setPhoto(userID, profilePhoto)
    }

    private suspend fun getUserPhoto(){
        user.getUserPhoto(userID)
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