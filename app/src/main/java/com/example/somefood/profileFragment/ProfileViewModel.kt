package com.example.appsomefood.profileFragment

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.repository.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repositoryProfileData: RepositoryProfileData,
    private val repositoryUser: RepositoryUser,
    private val repositoryOrders: RepositoryOrders,
    private val repositoryFood: RepositoryFood,
    private val preference: Reference
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

    private fun takeFeedbackProfile(it: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.takeProfileInfo(it).collect {
                feedbackProfile.value = it
                takeRatingForFeedback()
                repositoryOrders.takeForRVLastest(repositoryUser.pref, status = Status.ARCHIVE).collect {
                    _listFoodsForRecycler.value = it
                }
            }
        }
    }

    private fun takeRatingForFeedback() {
        val stat = _userFeedback.value?.id?.value?.isCreator
        viewModelScope.launch(Dispatchers.IO) {
            if (stat == true) {
                repositoryOrders.takeRatingForFeedbackByCreator(_userFeedback.value?.id?.value?.uuid.toString())
                    .collect {
                        val list = it?.map { it.markForClient }
                        if (list != null) {
                            _ratingFeedback.value = list.filterNotNull().average()
                        }
                    }
            } else if (stat == false) {
                repositoryOrders.takeRatingForFeedbackByClient(_userFeedback.value?.id?.value?.uuid.toString())
                    .collect {
                        val list = it?.map { it.markForCreator }
                        if (list != null) {
                            _ratingFeedback.value = list.filterNotNull().average()
                        }
                    }
            }
        }
    }

    private fun takeMarksForProfileCreator() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryOrders.takeMarksForCreator(repositoryUser.pref).collect {
                val list = it?.map { it.markForCreator }
                if (list != null) {
                    _averageMark.value = list.filterNotNull().average()
                }
            }
        }
    }

    private fun setPhotoProfile(profilePhoto: String) {
        viewModelScope.launch {
            repositoryUser.setPhoto(repositoryUser.pref, profilePhoto)
        }
    }

    fun savePhoto(newUri: Uri?) {
        viewModelScope.launch {
            setPhotoProfile(repositoryUser.savePhoto(newUri))
        }
    }

    private fun takeMarksForProfileClient() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryOrders.takeMarksForClient(repositoryUser.pref).collect {
                val list = it?.map { it.markForClient }
                if (list != null) {
                    _averageMark.value = list.filterNotNull().average()
                }
            }
        }
    }

    fun signOut() {
        preference.remove("pref")
    }

    fun setName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateName(name, repositoryUser.pref)
        }
    }


    fun setDescription(des: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateDescription(des, repositoryUser.pref)
        }
    }

    fun setAddress(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryProfileData.updateAddress(address, repositoryUser.pref)
        }
    }

    fun takeProfileInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUser.takeProfileInfo(repositoryUser.pref).collect {
                _profile.value = it
                if (it.isCreator == true) {
                    takeMarksForProfileCreator()
                    repositoryOrders.takeFeedbackForCreator(repositoryUser.pref).collect {
                        if (it != null) {
                            takeFeedbackProfile(it.idUser)
                        }
                        _userFeedback.value =
                            ProfileForFeedback(
                                feedbackProfile,
                                it?.textForCreator,
                                it?.markForCreator
                            )
                    }
                } else {
                    takeMarksForProfileClient()
                    repositoryOrders.takeFeedbackForClient(repositoryUser.pref).collect {
                        it?.idCreator?.let { it1 -> takeFeedbackProfile(it1) }
                        _userFeedback.value =
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
            repositoryProfileData.changeStatus(repositoryUser.pref, creatorStatus)
        }
    }

    fun takeAllOrdersForMost() {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryOrders.takeAllOrders(repositoryUser.pref, Status.ARCHIVE).collect {
                if (it != null) {
                    if (it.isNotEmpty()) {
                        mostCommon(repositoryUser.pref, it.map { it.name })
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