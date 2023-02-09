package com.example.appsomefood.profileFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        initUserViews()
        initCountOrders()
        initMostOrder()
        observeLastFeedback()
        observeLastOrders()
    }

    private fun initUserViews() {
        with(viewBinding) {
            with(recyclerViewForLatestOrders) {
                layoutManager = LinearLayoutManager(
                    context
                )
                adapter = fastAdapter
            }
            viewModelProfile.dataAll.onEach {
                if (it?.user != null) {
                    val markAverage = String.format("%.1f", it.user.averageMark)
                    mark.text = "${markAverage}/5"
                    idProfile.text = "${it.user.uuid}"
                    loginProfile.text = " ${it.user.login}"
                    profileNameHeader.setText("${it.user.name}")
                    textInfo.setText("${it.user.description}")
                    addressProfile.setText("${it.user.address}")
                    Glide
                        .with(imageViewProfile.context)
                        .asBitmap()
                        .load(it.user.icon)
                        .centerCrop()
                        .placeholder(R.drawable.aheg)
                        .into(imageViewProfile)
                }
                if (it?.user?.isCreator == false) {
                    btnPersonProfile.isChecked = false
                    nonCreatorProfile.isVisible = true
                    creatorProfile.isVisible = false
                } else {
                    btnPersonProfile.isChecked = true
                    creatorProfile.isVisible = true
                    nonCreatorProfile.isVisible = false
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

    private fun initCountOrders(){
        viewModelProfile.dataAll.onEach {
            if ( it?.order!= null){
                with(viewBinding){
                    ordersDoneNumber.text = it.order.ordersDone
                    ordersGoneNumber.text = it.order.ordered
                }
            }
        }.launchIn(viewModelProfile.viewModelScope)
    }


    private fun initMostOrder(){
        viewModelProfile.dataAll.onEach {
            if (it?.counter!= null){
                with(viewBinding){
                    nameMostOrderedFood.text = it.counter.food?.name
                    numberOrdered.text = "Ordered: ${it.counter.count}"
                    Glide
                        .with(imageViewMostOrderedFood.context)
                        .load(it.counter.food?.image)
                        .into(imageViewMostOrderedFood)
                }
            }
        }.launchIn(viewModelProfile.viewModelScope)
    }


    private fun changeStatus(i: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelProfile.changeStatus(i)
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

    private fun observeLastFeedback(){
        viewModelProfile.dataAll.onEach {
            with(viewBinding){
                nameLastFeedback.text = it?.feedback?.profile?.name
                Glide
                    .with(imageViewLastFeedback.context)
                    .load(it?.feedback?.profile?.icon)
                    .into(imageViewLastFeedback)
                val markForFeedback = String.format("%.1f", it?.feedback?.markFeedback)
                val markByFeedback = String.format("%.1f", it?.feedback?.markByFeedback)
                feedbackMark.text = markByFeedback
                markFeedback.text = "$markForFeedback/5"
                textViewFeedback.text = it?.feedback?.text
            }
        }.launchIn(viewModelProfile.viewModelScope)
    }

    private fun observeLastOrders() {
        viewModelProfile.dataAll.filterNotNull().onEach {
            itemAdapter.set(it.listLast.map {
                ListLastestItem(it)
            })
            Log.e("hoolie", "12${it}")
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}