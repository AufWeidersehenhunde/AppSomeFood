package com.example.appsomefood.profileFragment

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appsomefood.DBandProvider.FoodDb
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
    private val _profile = MutableStateFlow<UsersDb?>(null)
    val profile: MutableStateFlow<UsersDb?> = _profile
    private val _listFoodsForRecycler = MutableStateFlow<List<OrdersModel>?>(null)
    val listFoodsForRecycler: MutableStateFlow<List<OrdersModel>?> = _listFoodsForRecycler
    private val _dataProfile = MutableStateFlow<DataForFragment?>(null)
    val dataProfile: MutableStateFlow<DataForFragment?> = _dataProfile


    private val _dataUser = MutableStateFlow<UserDataState?>(null)
    val dataUser: MutableStateFlow<UserDataState?> = _dataUser


    init {
        takeUserData()
    }

    private fun takeUserData() {
        viewModelScope.launch {
            repositoryUser.getProfileInfo(repositoryUser.userID)?.let { info ->
                Log.e("dataP", "kekes $info")
                _dataUser.value = UserDataState(
                    uuid = info.uuid,
                    login = info.login,
                    description = info.description,
                    icon = info.icon,
                    address = info.address,
                    name = info.name,
                    isCreator = info.isCreator
                )
            }
        }
    }

    private fun takeOrdersData(){
        viewModelScope.launch {
//            repositoryOrders.takeOrdersOrdered()
        }
    }


    private fun takeordersData() {
        viewModelScope.launch {
//            repositoryOrders.observeAllOrders()
        }
    }

    private fun observeFeedbackProfile(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _dataProfile.update {
                it?.copy(
                    feedbackUser = ProfileForFeedback(
                        feedbackProfile = repositoryUser.getProfileInfo(id)
                    )
                )
            }
            Log.e("hhh", "prof${_dataProfile.value?.feedbackUser?.feedbackProfile}")
            observeRatingForFeedback()
            takeListLast()
            Log.e("hhh", "11${repositoryUser.getProfileInfo(id)}")
        }
    }


    private fun takeListLast() {
        viewModelScope.launch {
            repositoryUser.userID.let { it1 ->
                repositoryOrders.observeForRVLastest(it1, status = Status.ARCHIVE).collect {
                    _listFoodsForRecycler.value = it
                    Log.e("hhh", "22${it}")
                }
            }
        }
    }

    private fun observeRatingForFeedback() {
        viewModelScope.launch(Dispatchers.IO) {
            val stat = _dataProfile.value?.feedbackUser?.feedbackProfile?.isCreator
            Log.e("hhh", "336${_dataProfile.value?.feedbackUser?.feedbackProfile}")
            if (stat == true) {
                repositoryOrders.observeRatingForFeedbackByCreator(_dataProfile.value?.feedbackUser?.feedbackProfile?.uuid.toString())
                    .collect {
                        val list = it?.map { it.markForClient }
                        if (list != null) {
                            _dataProfile.update {
                                it?.copy(
                                    ratingFeedback = list.filterNotNull().average()
                                )
                            }
                        }
                        Log.e("hhh", "33${it}")
                    }
            } else if (stat == false) {
                repositoryOrders.observeRatingForFeedbackByClient(_dataProfile.value?.feedbackUser?.feedbackProfile?.uuid.toString())
                    .collect {
                        val list = it?.map { it.markForCreator }
                        if (list != null) {
                            _dataProfile.update {
                                it?.copy(
                                    ratingFeedback = list.filterNotNull().average()
                                )
                            }
                        }
                        Log.e("hhh", "332${it}")
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
                        _dataProfile.update {
                            it?.copy(
                                averageMark = list.filterNotNull().average()
                            )
                        }
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
                        _dataProfile.update {
                            it?.copy(
                                averageMark = list.filterNotNull().average()
                            )
                        }
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


    private fun observeProfileInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            _profile.value = repositoryUser.getProfileInfo(repositoryUser.userID)
            if (repositoryUser.getProfileInfo(repositoryUser.userID)?.isCreator == true) {
                observeMarksForProfileCreator()
                Log.e("hhh", "22123${_profile.value}")
                // suspend
                repositoryOrders.observeFeedbackForCreator(repositoryUser.userID)
                    .collect { fb ->
                        Log.e("hhh", "22123123${fb}")
                        if (!fb.isNullOrEmpty()) {
                            val order = fb.last()
                            observeFeedbackProfile(order.idUser)
                            _dataProfile.update {
                                it?.copy(
                                    feedbackUser = ProfileForFeedback(
                                        _dataProfile.value?.feedbackUser?.feedbackProfile,
                                        order.textForCreator,
                                        order.markForCreator
                                    )
                                )
                            }

                        }
                    }
            } else {
                observeMarksForProfileClient()
                Log.e("hhh", "22123${_profile.value}")
                repositoryOrders.observeFeedbackForClient(repositoryUser.userID).collect {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            val order = it.last()
                            order.idCreator?.let { it1 -> observeFeedbackProfile(it1) }
                            _dataProfile.update {
                                it?.copy(
                                    feedbackUser =
                                    ProfileForFeedback(
                                        _dataProfile.value?.feedbackUser?.feedbackProfile,
                                        order.textForClient,
                                        order.markForClient
                                    )
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
            _dataProfile.update {
                it?.copy(
                    ordersCount = OrdersCount(
                        repositoryOrders.takeOrdersDone(uuid, Status.ARCHIVE),
                        repositoryOrders.takeOrdersOrdered(uuid, Status.ARCHIVE),
                        numbersByElement.maxBy { it.value }.value,
                        repositoryFood.observeFoodForMustOrder(numbersByElement.maxBy { it.value }.key)
                    )
                )
            }

        }
    }


    data class OrdersCount(
        var oredered: Int = 0,
        val done: Int = 0,
        var mustOrderedNum: Int? = 0,
        var mustOrderedFood: FoodDb? = FoodDb("", "", "", "")
    )

    data class ProfileForFeedback(
        val feedbackProfile: UsersDb? = null,
        val text: String? = "",
        val mark: Double? = 0.00
    )

    data class DataForFragment(
        val ordersCount: OrdersCount?,
        val feedbackUser: ProfileForFeedback?,
        val averageMark: Double? = 0.00,
        val ratingFeedback: Double? = (0.00)
    )

    data class UserDataState(
        val uuid: String,
        val login: String,
        val description: String = "",
        val name: String = "User",
        val address: String = "",
        val icon: String? = null,
        val isCreator: Boolean? = null,
    )
}