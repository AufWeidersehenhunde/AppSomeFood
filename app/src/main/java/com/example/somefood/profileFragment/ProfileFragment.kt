package com.example.appsomefood.profileFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentProfileBinding
import com.example.appsomefood.MainActivity.MainActivity
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
//        checkStatus()
        initView()
//        takeData()
        takeAllData()
        observeElement()


        initUserViews()
    }

    private fun initUserViews() {
        with(viewBinding) {
            viewModelProfile.dataUser.onEach {
                if (it != null) {
                    idProfile.text = "${it.uuid}"
                    loginProfile.text = " ${it.login}"
                    profileNameHeader.setText("${it.name}")
                    textInfo.setText("${it.description}")
                    addressProfile.setText("${it.address}")
                    Glide
                        .with(imageViewProfile.context)
                        .asBitmap()
                        .load(it.icon)
                        .centerCrop()
                        .placeholder(R.drawable.aheg)
                        .into(imageViewProfile)
                }
                if (it?.isCreator == false) {
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

    private fun initView() {
        viewModelProfile.observeAllOrdersForMost()
        with(viewBinding) {
            with(recyclerViewForLatestOrders) {
                layoutManager = LinearLayoutManager(
                    context
                )
                adapter = fastAdapter
            }

            btnSignOutAcc.setOnClickListener {
                viewModelProfile.signOut()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }

            Glide.with(imageViewLastFeedback.context)
                .load(R.drawable.aheg)
                .into(imageViewLastFeedback)


        }
    }

    private fun takeAllData(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelProfile.dataProfile.filterNotNull().collect {
                with(viewBinding) {
                    Log.e("hhh", "$it")
                    val markRound = String.format("%.1f", it.averageMark)
                    mark.text = "$markRound/5"
                    ordersDoneNumber.text = it.ordersCount?.oredered.toString()
                    ordersGoneNumber.text = it.ordersCount?.done.toString()
                    Glide.with(imageViewMostOrderedFood.context)
                        .load(it.ordersCount?.mustOrderedFood?.image)
                        .into(imageViewMostOrderedFood)
                    numberOrdered.text = "Ordered:  ${it.ordersCount?.mustOrderedNum}"
                    nameMostOrderedFood.text = it.ordersCount?.mustOrderedFood?.name
                    val markRoundFeedback = String.format("%.1f", it.ratingFeedback)
                    feedbackMark.text = "$markRoundFeedback"
                    textViewFeedback.text = "${it.feedbackUser?.text}"
                    nameLastFeedback.text = "${it.feedbackUser?.feedbackProfile?.name}"
                    markFeedback.text = "${it.feedbackUser?.mark}"
                }
            }
        }
    }

    private fun changeStatus(i: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelProfile.changeStatus(i)
        }
    }

//    private fun checkStatus() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModelProfile.profile.filterNotNull().collect {
//                    with(viewBinding) {
//                        if (it.isCreator == false) {
//                            btnPersonProfile.isChecked = false
//                            nonCreatorProfile.isVisible = true
//                            creatorProfile.isVisible = false
//                        } else {
//                            btnPersonProfile.isChecked = true
//                            creatorProfile.isVisible = true
//                            nonCreatorProfile.isVisible = false
//                        }
//                    }
//                }
//
//            }
//        }
//    }

//    private fun takeData() {
//        with(viewBinding) {
//            changeView(profileNameHeader, acceptName)
//            changeView(textInfo, acceptDescription)
//            changeView(addressProfile, acceptAddress)
//            viewLifecycleOwner.lifecycleScope.launch {
//                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                    viewModelProfile.profile.filterNotNull().collect {
//                        idProfile.text = "${it.uuid}"
//                        loginProfile.text = " ${it.login}"
//                        profileNameHeader.setText("${it.name}")
//                        textInfo.setText("${it.description}")
//                        addressProfile.setText("${it.address}")
//                        Glide
//                            .with(imageViewProfile.context)
//                            .asBitmap()
//                            .load(it.icon)
//                            .centerCrop()
//                            .placeholder(R.drawable.aheg)
//                            .into(imageViewProfile)
//                    }
//                }
//            }
//        }
//    }




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

    private fun observeElement() {
        viewModelProfile.listFoodsForRecycler.filterNotNull().onEach {
            itemAdapter.set(it.map {
                ListLastestItem(it)
            })
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}