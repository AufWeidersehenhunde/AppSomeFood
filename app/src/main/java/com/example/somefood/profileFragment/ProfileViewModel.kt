package com.example.appsomefood.profileFragment

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.Feedback
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.DBandProvider.Orders
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.MainActivity.MainActivity
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.Screens
import com.example.appsomefood.repository.*
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class ProfileViewModel(
    private val repositoryProfileData: RepositoryProfileData,
    private val repositorySQL: RepositorySQL,
    private val repositoryOrders: RepositoryOrders,
    private val repositoryFood: RepositoryFood,
    private val preference: Reference
) : ViewModel() {
    val user = preference.getValue("pref").toString()
    private val _profile = MutableStateFlow<UsersDb?>(null)
    val profile: MutableStateFlow<UsersDb?> = _profile
    private val _ordersCount = MutableStateFlow<OrdersCount?>(null)
    val ordersCount: MutableStateFlow<OrdersCount?> = _ordersCount
    val listFoodsForRecycler = MutableStateFlow<List<OrdersModel>?>(null)
    var userFeedback = MutableStateFlow<ProfileForFeedback?>(null)
    private val feedbackProfile = MutableStateFlow<UsersDb?>(null)
    var averageMark = MutableStateFlow<Double>(0.00)
    val ratingFeedback = MutableStateFlow<Double>(0.00)

    private fun takeFeedbackProfile(it: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositorySQL.takeProfileInfo(it).collect {
                feedbackProfile.value = it
                takeRatingForFeedback()
                repositoryOrders.takeForRVLastest(user, status = Status.ARCHIVE).collect {
                    listFoodsForRecycler.value = it
                }
            }
        }
    }

    private fun takeRatingForFeedback() {
        val stat = userFeedback.value?.id?.value?.isCreator
        viewModelScope.launch(Dispatchers.IO) {
            if (stat == true) {
                repositoryOrders.takeRatingForFeedbackByCreator(userFeedback.value?.id?.value?.uuid.toString())
                    .collect {
                        val list = it?.map { it.markForClient }
                        if (list != null) {
                            ratingFeedback.value = list.filterNotNull().average()
                        }
                    }
            } else if (stat == false) {
                repositoryOrders.takeRatingForFeedbackByClient(userFeedback.value?.id?.value?.uuid.toString())
                    .collect {
                        val list = it?.map { it.markForCreator }
                        if (list != null) {
                            ratingFeedback.value = list.filterNotNull().average()
                        }
                    }
            }
        }
    }

    private fun takeMarksForProfileCreator() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryOrders.takeMarksForCreator(user).collect {
                val list = it?.map { it.markForCreator }
                if (list != null) {
                    averageMark.value = list.filterNotNull().average()
                }
            }
        }
    }

    private fun setPhotoProfile(profilePhoto: String) {
        viewModelScope.launch {
            repositorySQL.setPhoto(user, profilePhoto)
        }
    }

    fun savePhoto(newUri: Uri?) {
        viewModelScope.launch {
            setPhotoProfile(repositorySQL.savePhoto(newUri))
        }
    }

    private fun takeMarksForProfileClient() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryOrders.takeMarksForClient(user).collect {
                val list = it?.map { it.markForClient }
                if (list != null) {
                    averageMark.value = list.filterNotNull().average()
                }
            }
        }
    }

    fun signOut() {
        preference.remove("pref")
    }

    fun setName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateName(name, user)
        }
    }


    fun setDescription(des: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateDescription(des, user)
        }
    }

    fun setAddress(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateAddress(address, user)
        }
    }

    fun takeProfileInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            repositorySQL.takeProfileInfo(user).collect {
                _profile.value = it
                if (it.isCreator == true) {
                    takeMarksForProfileCreator()
                    repositoryOrders.takeFeedbackForCreator(user).collect {
                        if (it != null) {
                            takeFeedbackProfile(it.idUser)
                        }
                        userFeedback.value =
                            ProfileForFeedback(
                                feedbackProfile,
                                it?.textForCreator,
                                it?.markForCreator
                            )
                    }
                } else {
                    takeMarksForProfileClient()
                    repositoryOrders.takeFeedbackForClient(user).collect {
                        it?.idCreator?.let { it1 -> takeFeedbackProfile(it1) }
                        userFeedback.value =
                            ProfileForFeedback(
                                feedbackProfile,
                                it?.textForClient,
                                it?.markForClient
                            )
                    }
                }
            }
        }
    }


    fun changeStatus(creatorStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.changeStatus(user, creatorStatus)
        }
    }

    fun takeAllOrdersForMost() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryOrders.takeAllOrders(user, Status.ARCHIVE).collect {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        mostCommon(user, it.map { it.name })
                    }
                }
            }
        }
    }

    private fun mostCommon(uuid: String, input: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val numbersByElement = input.groupingBy { it }.eachCount()
            _ordersCount.value = OrdersCount(
                repositoryOrders.takeOrdersDone(uuid, Status.ARCHIVE),
                repositoryOrders.takeOrdersOrdered(uuid, Status.ARCHIVE),
                numbersByElement.maxBy { it.value }.value,
                repositoryFood.takeFoodForMustOrder(numbersByElement.maxBy { it.value }.key)
            )
        }
    }


    data class OrdersCount(
        var oredered: Int,
        val done: Int,
        var mustOrderedNum: Int?,
        var mustOrderedFood: FoodDb?
    )

    data class ProfileForFeedback(
        var id: MutableStateFlow<UsersDb?>,
        var text: String?,
        var mark: Double?
    )

}