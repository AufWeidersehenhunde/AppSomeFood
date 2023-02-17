package com.example.appsomefood.profileFragment

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.Orders
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.*
import com.example.somefood.AllScreens.ProfileFragment.DataProfile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repositoryProfileData: RepositoryProfileData,
    private val repositoryUser: RepositoryUser,
    private val repositoryOrders: RepositoryOrders,
    private val repositoryFood: RepositoryFood
) : ViewModel() {

    private val _dataAll = MutableStateFlow<AllDataProfile?>(AllDataProfile())
    val dataAll: MutableStateFlow<AllDataProfile?> = _dataAll


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

                _dataAll.update { model ->
                    model?.copy(
                        user = UserDataState(
                            uuid = info.uuid,
                            login = info.login,
                            averageMark = rate.filterNotNull().average(),
                            description = info.description,
                            icon = info.icon,
                            address = info.address,
                            name = info.name,
                            isCreator = info.isCreator
                        )
                    )
                }
            }
        }
    }

    private fun takeOrdersStats() {
        viewModelScope.launch(Dispatchers.IO) {
            _dataAll.update { model ->
                model?.copy(
                    order = OrdersStats(
                        ordered = repositoryOrders.takeOrdersOrdered(repositoryUser.userID)
                            .toString(),
                        ordersDone = repositoryOrders.takeOrdersDone(repositoryUser.userID)
                            .toString()
                    )
                )
            }
        }
    }

    private fun takeMostOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            val orders = repositoryOrders.observeAllOrders(repositoryUser.userID)
            if (orders.isNotEmpty()) {
                val numbersByElement = orders.map { it?.name }.groupingBy { it }.eachCount()
                _dataAll.update { model ->
                    model?.copy(
                        counter = MostOrdered(
                            food = numbersByElement.maxBy { it.value }.key?.let {
                                repositoryFood.observeFoodForMustOrder(it)
                            },
                            count = numbersByElement.maxBy { it.value }.value.toString(),
                        )
                    )
                }
            }
        }
    }

    private fun takeLastFeedback() {
        viewModelScope.launch(Dispatchers.IO) {
            val element = repositoryUser.getProfileInfo(repositoryUser.userID)
            val list =
                repositoryOrders.getFeedback(repositoryUser.userID, element?.isCreator).lastOrNull()
            val user: UsersDb?
            val rate: List<Orders>
            val listMarks: List<Double?>
            val textFeedback:String?
            val mark: Double?
            if (list != null) {
                if (element?.isCreator == true) {
                    rate = repositoryOrders.observeRatingForFeedbackByCreator(list.idCreator)
                    user = repositoryUser.getProfileInfo(list.idUser)
                    listMarks = rate.map { it.markForClient }
                    textFeedback = list.textForCreator
                    mark = list.markForCreator
                } else {
                    rate = repositoryOrders.observeRatingForFeedbackByClient(list.idUser)
                    user = repositoryUser.getProfileInfo(list.idCreator)
                    listMarks = rate.map { it.markForCreator }
                    textFeedback = list.textForClient
                    mark = list.markForClient
                }
                _dataAll.update {
                    it?.copy(
                        feedback = LastFeedback(
                            profile = user,
                            text = textFeedback,
                            markFeedback = mark,
                            markByFeedback = listMarks.filterNotNull().average()
                        )
                    )
                }
            }
        }
    }


    private fun takeListLast() {
        viewModelScope.launch(Dispatchers.IO) {
            _dataAll.update {
                it?.copy(
                    listLast = repositoryOrders.observeForRVLastest(
                        repositoryUser.userID,
                        status = Status.ARCHIVE
                    )
                )
            }
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
}


