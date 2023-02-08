package com.example.appsomefood.profileFragment

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.Screens
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
    private val _profile = MutableStateFlow<UsersDb?>(null)
    val profile: MutableStateFlow<UsersDb?> = _profile
    private val _ordersCount = MutableStateFlow<OrdersCount?>(null)
    val ordersCount: MutableStateFlow<OrdersCount?> = _ordersCount
    private val _listFoodsForRecycler = MutableStateFlow<List<OrdersModel>?>(null)
    val listFoodsForRecycler: MutableStateFlow<List<OrdersModel>?> = _listFoodsForRecycler
    private var _userFeedback = MutableStateFlow<ProfileForFeedback?>(null)
    var userFeedback: MutableStateFlow<ProfileForFeedback?> = _userFeedback
    private val feedbackProfile = MutableStateFlow<UsersDb?>(null)
    private var _averageMark = MutableStateFlow<Double>(0.00)
    var averageMark: MutableStateFlow<Double> = _averageMark
    private val _ratingFeedback = MutableStateFlow<Double>(0.00)
    val ratingFeedback: MutableStateFlow<Double> = _ratingFeedback

    val example = MutableStateFlow<DataForFragment?>(null)


    private fun observeFeedbackProfile(it: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.observeProfileInfo(it).collect {
                example.value?.id = it
                feedbackProfile.value = it
                observeRatingForFeedback()
                repositoryUser.userID.let { it1 ->
                    repositoryOrders.observeForRVLastest(it1, status = Status.ARCHIVE).collect {
                        _listFoodsForRecycler.value = it
                    }
                }
            }
        }
    }

    private fun observeRatingForFeedback() {
        val stat = _userFeedback.value?.id?.value?.isCreator
        viewModelScope.launch(Dispatchers.IO) {
            if (stat == true) {
                repositoryOrders.observeRatingForFeedbackByCreator(_userFeedback.value?.id?.value?.uuid.toString())
                    .collect {
                        val list = it?.map { it.markForClient }
                        if (list != null) {
                            _ratingFeedback.value = list.filterNotNull().average()
                        }
                    }
            } else if (stat == false) {
                repositoryOrders.observeRatingForFeedbackByClient(_userFeedback.value?.id?.value?.uuid.toString())
                    .collect {
                        val list = it?.map { it.markForCreator }
                        if (list != null) {
                            _ratingFeedback.value = list.filterNotNull().average()
                        }
                    }
            }
        }
    }

    private fun observeMarksForProfileCreator() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.userID.let {
                repositoryOrders.observeFeedbackForCreator(it).collect {
                    val list = it?.map { it.markForCreator }
                    if (list != null) {
                        _averageMark.value = list.filterNotNull().average()
                    }
                }
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

    private fun observeMarksForProfileClient() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.userID.let {
                repositoryOrders.observeFeedbackForClient(it).collect {
                    val list = it?.map { it.markForClient }
                    if (list != null) {
                        _averageMark.value = list.filterNotNull().average()
                    }
                }
            }
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

    fun observeProfileInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.observeProfileInfo(repositoryUser.userID).collect {
                _profile.value = it
                if (it.isCreator == true) {
                    observeMarksForProfileCreator()
                    repositoryOrders.observeFeedbackForCreator(repositoryUser.userID)
                        .collect {
                            if (it != null) {
                                if (it.isNotEmpty()) {
                                    val order = it.last()
                                    observeFeedbackProfile(order.idUser)
                                    _userFeedback.value =
                                        ProfileForFeedback(
                                            feedbackProfile,
                                            order.textForCreator,
                                            order.markForCreator
                                        )
                                }
                            }
                        }
                } else {
                    observeMarksForProfileClient()
                    repositoryOrders.observeFeedbackForClient(repositoryUser.userID).collect {
                        if (it != null) {
                            if (it.isNotEmpty()) {
                                val order = it.last()
                                order.idCreator?.let { it1 -> observeFeedbackProfile(it1) }
                                _userFeedback.value =
                                    ProfileForFeedback(
                                        feedbackProfile,
                                        order.textForClient,
                                        order.markForClient
                                    )
                            }
                        }
                    }
                }
            }
        }
    }


    fun changeStatus(creatorStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.changeStatus(repositoryUser.userID, creatorStatus)
        }
    }

    fun observeAllOrdersForMost() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryOrders.observeAllOrders(repositoryUser.userID, Status.ARCHIVE).collect {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        observeMostCommon(repositoryUser.userID, it.map { it.name })
                    }
                }
            }
        }
    }

    private fun observeMostCommon(uuid: String, input: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val numbersByElement = input.groupingBy { it }.eachCount()
            _ordersCount.value = OrdersCount(
                repositoryOrders.takeOrdersDone(uuid, Status.ARCHIVE),
                repositoryOrders.takeOrdersOrdered(uuid, Status.ARCHIVE),
                numbersByElement.maxBy { it.value }.value,
                repositoryFood.observeFoodForMustOrder(numbersByElement.maxBy { it.value }.key)
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

    data class DataForFragment(
        var ordered: Int,
        val ordersDone: Int,
        var mustOrderedNum: Int?,
        var mustOrderedFood: FoodDb?,
        var text: String?,
        var mark: Double?,
        var id: UsersDb?
    )
}