package com.example.appsomefood.profileFragment

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.DBandProvider.Orders
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.*
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repositoryProfileData: RepositoryProfileData,
    private val repositoryUser: RepositoryUser,
    private val repositoryOrders: RepositoryOrders,
    private val repositoryFood: RepositoryFood,
    private val router: Router,
) : ViewModel() {

    private val _dataAll = MutableStateFlow<AllDataEbanata?>(null)
    val dataAll: MutableStateFlow<AllDataEbanata?> = _dataAll


    init {
        takeUserData()
        takeMostOrder()
        takeOrdersStats()
        takeListLast()
        takeLastFeedback()
    }

    private fun takeUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.getProfileInfo(repositoryUser.userID)?.let { info ->
                val rate =
                    if (info.isCreator == true) {
                        repositoryOrders.observeRatingForFeedbackByClient(
                            repositoryUser.userID
                        )
                            .map { it.markForCreator }
                    } else {
                        repositoryOrders.observeRatingForFeedbackByCreator(
                            repositoryUser.userID
                        ).map { it.markForClient }
                    }

                _dataAll.update { it?.copy(
                    user = UserDataState( uuid = info.uuid,
                    login = info.login,
                    averageMark = rate.filterNotNull().average(),
                    description = info.description,
                    icon = info.icon,
                    address = info.address,
                    name = info.name,
                    isCreator = info.isCreator)
                ) }
            }
        }
    }

    private fun takeOrdersStats() {
        viewModelScope.launch(Dispatchers.IO) {
            _dataAll.update { it?.copy(
                order = OrdersStats(
                ordered = repositoryOrders.takeOrdersOrdered(repositoryUser.userID).toString(),
                ordersDone = repositoryOrders.takeOrdersDone(repositoryUser.userID).toString()
            )) }
        }
    }

    private fun takeMostOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            val orders = repositoryOrders.observeAllOrders(repositoryUser.userID)
            if (orders.isNotEmpty()) {
                val numbersByElement = orders.map { it?.name }.groupingBy { it }.eachCount()
                _dataAll.update { it?.copy(
                    counter = MostOrdered(
                        food = numbersByElement.maxBy { it.value }.key?.let {
                            repositoryFood.observeFoodForMustOrder(it)
                        },
                    count = numbersByElement.maxBy { it.value }.value.toString(),
                    )
                ) }
            }
        }
    }

    private fun takeLastFeedback() {
        viewModelScope.launch(Dispatchers.IO) {
            val element = repositoryUser.getProfileInfo(repositoryUser.userID)
            val list = repositoryOrders.getFeedback(repositoryUser.userID, element?.isCreator).lastOrNull()
            val user: UsersDb?
            val rate: List<Orders>
            val listMarks: List<Double?>
            if (list != null) {
                if (element?.isCreator==true){
                    rate = repositoryOrders.observeRatingForFeedbackByCreator(list.idCreator)
                    user = repositoryUser.getProfileInfo(list.idUser)
                    listMarks = rate.map { it.markForClient }
                } else{
                    rate = repositoryOrders.observeRatingForFeedbackByClient(list.idUser)
                    user = list.idCreator.let { repositoryUser.getProfileInfo(it) }
                    listMarks =  rate.map { it.markForCreator }
                }
                _dataAll.update { it?.copy(
                    feedback = LastFeedback(
                        profile = user,
                        text =
                        if (element?.isCreator == true) {
                            list.textForCreator
                        } else {
                            list.textForClient
                        },
                        markFeedback =
                        if (element?.isCreator == true) {
                            list.markForCreator
                        } else {
                            list.markForClient
                        },
                        markByFeedback = listMarks.filterNotNull().average()
                    )
                ) }
            }
        }
    }


    private fun takeListLast() {
        viewModelScope.launch {
            _dataAll.update { it?.copy(
                listLast = repositoryOrders.observeForRVLastest(repositoryUser.userID, status = Status.ARCHIVE)
            ) }
        }
    }


    private fun setPhotoProfile(profilePhoto: String) {
        viewModelScope.launch {
            repositoryUser.setPhoto(repositoryUser.userID, profilePhoto)
        }
    }

    fun savePhoto(newUri: Uri?) {
        viewModelScope.launch {
            setPhotoProfile(repositoryUser.savePhoto(newUri))
        }
    }


    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.delPref()
        }
    }

    fun setName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateName(name, repositoryUser.userID)
        }
    }


    fun setDescription(des: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateDescription(des, repositoryUser.userID)
        }
    }

    fun setAddress(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateAddress(address, repositoryUser.userID)
        }
    }


    fun changeStatus(creatorStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.changeStatus(repositoryUser.userID, creatorStatus)
        }
    }

    data class UserDataState(
        val uuid: String,
        val login: String,
        val averageMark: Double?,
        val description: String = "",
        val name: String = "User",
        val address: String = "",
        val icon: String? = null,
        val isCreator: Boolean? = null,
    )

    data class OrdersStats(
        val ordered: String,
        val ordersDone: String
    )

    data class MostOrdered(
        val food: FoodDb?,
        val count: String?
    )

    data class LastFeedback(
        val profile: UsersDb?,
        val text: String?,
        val markFeedback: Double?,
        val markByFeedback: Double?
    )

    data class AllDataEbanata(
        val user: UserDataState,
        val order: OrdersStats,
        val counter: MostOrdered,
        val feedback: LastFeedback,
        val listLast: List<OrdersModel?>
    )

}


