package com.example.appsomefood.profileFragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.appsomefood.MainActivity.MainActivity
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentProfileBinding
import com.example.appsomefood.PhotoProfile
import com.example.somefood.Services.hideKeyboard
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var photoProfile: PhotoProfile? = null
    private val viewBinding: FragmentProfileBinding by viewBinding()
    private val viewModelProfile: ProfileViewModel by viewModel()
    private val itemAdapter = ItemAdapter<ListLastestItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)
    private var btnBoolean: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoProfile =
            PhotoProfile(requireActivity().activityResultRegistry) { uri ->
                viewModelProfile.savePhoto(uri)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewUserData()
    }

    private fun initViewUserData() {
        with(viewBinding) {
            with(recyclerViewForLatestOrders) {
                layoutManager = LinearLayoutManager(
                    context
                )
                adapter = fastAdapter
            }
            viewModelProfile.dataAll.filterNotNull().onEach {
                val user = it.user
                val counter = it.counter
                val feedback = it.feedback
                val list = it.listLast
                if (user != null) {
                    val markAverage = String.format("%.1f", user.averageMark)
                    mark.text = "${markAverage}/5"
                    idProfile.text = "${user.uuid}"
                    loginProfile.text = " ${user.login}"
                    profileNameHeader.setText("${user.name}")
                    textInfo.setText("${user.description}")
                    addressProfile.setText("${user.address}")
                    Glide
                        .with(imageViewProfile.context)
                        .asBitmap()
                        .load(it.user.icon)
                        .centerCrop()
                        .placeholder(R.drawable.aheg)
                        .into(imageViewProfile)
                }
                if (user?.isCreator == false) {
                    btnPersonProfile.isChecked = false
                    nonCreatorProfile.isVisible = true
                    creatorProfile.isVisible = false
                } else {
                    btnPersonProfile.isChecked = true
                    creatorProfile.isVisible = true
                    nonCreatorProfile.isVisible = false
                }
                it.order?.let { it1 -> initViewOrderData(it1) }

                if (counter != null) {
                    initViewMostOrdered(counter)
                }
                if (feedback != null) {
                    initViewLastFeedback(feedback)
                }
                if (list != null) {
                    observeLastOrders(list)
                }
            }.launchIn(viewModelProfile.viewModelScope)

            addPhotoToProfile.setOnClickListener {
                photoProfile?.pickPhoto()
            }

            Glide.with(imageViewProfile.context)
                .load(R.drawable.faceanime)
                .into(imageViewProfile)

            btnSignOutAcc.setOnClickListener {
                viewModelProfile.signOut()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }

            btnPersonProfile.setOnCheckedChangeListener { _, isChecked ->
                changeStatus(isChecked)
                if (isChecked) {
                    nonCreatorProfile.isVisible = false
                    creatorProfile.isVisible = true
                } else {
                    creatorProfile.isVisible = false
                    nonCreatorProfile.isVisible = true
                }
            }
            changeView(profileNameHeader, acceptName)
            changeView(textInfo, acceptDescription)
            changeView(addressProfile, acceptAddress)
        }
    }


    private fun changeStatus(status: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelProfile.changeStatus(status)
        }
    }

    private fun initViewOrderData(order: ProfileViewModel.OrdersStats) {
        with(viewBinding) {
            ordersDoneNumber.text = order.ordersDone
            ordersGoneNumber.text = order.ordered
        }
    }

    private fun initViewMostOrdered(counter: ProfileViewModel.MostOrdered) {
        with(viewBinding) {
            nameMostOrderedFood.text = counter.food?.name
            numberOrdered.text = "Ordered: ${counter.count}"
            Glide
                .with(imageViewMostOrderedFood.context)
                .load(counter.food?.image)
                .into(imageViewMostOrderedFood)
        }
    }

    private fun initViewLastFeedback(feedback: ProfileViewModel.LastFeedback) {
        with(viewBinding) {
            nameLastFeedback.text = feedback.profile?.name
            Glide
                .with(imageViewLastFeedback.context)
                .load(feedback.profile?.icon)
                .into(imageViewLastFeedback)
            val markForFeedback = String.format("%.1f", feedback.markFeedback)
            val markByFeedback = String.format("%.1f", feedback.markByFeedback)
            feedbackMark.text = markByFeedback
            markFeedback.text = "$markForFeedback/5"
            textViewFeedback.text = feedback.text
        }
    }


    private fun changeView(text: EditText, btn: ImageView) {
        text.setOnClickListener {
            text.isFocusable
            if (text.isFocusable) {
                btn.isVisible = true
            }
        }

        btn.setOnClickListener { view ->
            text.isFocusable = false
            btn.isVisible = false
            viewBinding.textInfo.isFocusable = true
            view.hideKeyboard()

            if (text == viewBinding.profileNameHeader) {
                text.setText("${text.text}")
                viewModelProfile.setName(
                    "${viewBinding.profileNameHeader.text}"
                )
            } else if (text == viewBinding.textInfo) {
                text.setText("${viewBinding.textInfo.text}")
                btnBoolean = false
                viewModelProfile.setDescription(
                    "${viewBinding.textInfo.text}"
                )
            } else {
                text.setText("${viewBinding.addressProfile.text}")
                btnBoolean = false
                viewModelProfile.setAddress(
                    "${viewBinding.addressProfile.text}"
                )
            }
        }
    }


    private fun observeLastOrders(list: List<OrdersModel?>) {
        itemAdapter.set(list.map { elements ->
            ListLastestItem(elements)
        })
    }
}