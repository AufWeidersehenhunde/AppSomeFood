package com.example.appsomefood.profileFragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentProfileBinding
import com.example.appsomefood.MainActivity.MainActivity
import com.example.appsomefood.PhotoProfile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private var photoProfile:PhotoProfile? = null
    private val viewBinding: FragmentProfileBinding by viewBinding()
    private val viewModelProfile: ProfileViewModel by viewModel()
    private var adapterLastest: RecyclerVIewAdapterLastestOrders? = null
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
        checkStatus()
        initView()
        takeData()
        takeDataForFeedback()
        observeElement()
        takeFeedbackRating()
    }

    private fun initView() {
        adapterLastest =
            RecyclerVIewAdapterLastestOrders()
        with(viewBinding) {
            with(recyclerViewForLatestOrders) {
                layoutManager = LinearLayoutManager(
                    context
                )
                adapter = adapterLastest
            }
            addPhotoToProfile.setOnClickListener {
              photoProfile?.pickPhoto()
            }
            btnSignOutAcc.setOnClickListener {
                viewModelProfile.signOut()
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
            }


            Glide.with(imageViewProfile.context)
                .load(R.drawable.faceanime)
                .into(imageViewProfile)

            Glide.with(imageViewLastFeedback.context)
                .load(R.drawable.aheg)
                .into(imageViewLastFeedback)
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModelProfile.averageMark.collect{
                        val markRound = String.format("%.1f", it)
                        mark.text = "$markRound/5"
                    }
                }
            }

            btnPersonProfile.setOnCheckedChangeListener { _, isChecked ->
                changeStatus(isChecked)
                if (isChecked) {
                    nonCreatorProfile.visibility = View.INVISIBLE
                    creatorProfile.visibility = View.VISIBLE
                } else {
                    creatorProfile.visibility = View.INVISIBLE
                    nonCreatorProfile.visibility = View.VISIBLE
                }
            }

            viewModelProfile.takeAllOrdersForMost()
            viewModelProfile.ordersCount.filterNotNull().onEach {
                ordersDoneNumber.text = it.oredered.toString()
                ordersGoneNumber.text = it.done.toString()
                Glide.with(imageViewMostOrderedFood.context)
                    .load(it.mustOrderedFood?.image)
                    .into(imageViewMostOrderedFood)
                numberOrdered.text = "Ordered:  ${it.mustOrderedNum}"
                nameMostOrderedFood.text = it.mustOrderedFood?.name
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun changeStatus(i: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
                viewModelProfile.changeStatus(i)
        }
    }

    private fun checkStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelProfile.profile.filterNotNull().collect {
                    with(viewBinding) {
                        if (it.isCreator == false) {
                            btnPersonProfile.isChecked = false
                            nonCreatorProfile.isVisible = true
                            creatorProfile.isVisible = false
                        } else {
                            btnPersonProfile.isChecked = true
                            creatorProfile.visibility = View.VISIBLE
                            nonCreatorProfile.visibility = View.INVISIBLE
                        }
                    }
                }

            }
        }
    }

    private fun View.hideKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewBinding.textInfo.focusable = View.FOCUSABLE
        }
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun takeData() {
        with(viewBinding) {
            viewModelProfile.takeProfileInfo()
            changeView(profileNameHeader, acceptName)
            changeView(textInfo, acceptDescription)
            changeView(addressProfile, acceptAddress)
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModelProfile.profile.filterNotNull().collect {
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
                }
            }
        }
    }

    private fun takeFeedbackRating(){
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModelProfile.ratingFeedback.collect{
                val markRoundFeedback = String.format("%.1f", it)
                viewBinding.feedbackMark.text = "$markRoundFeedback"
            }
        }

    }
    private fun takeDataForFeedback() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelProfile.userFeedback.filterNotNull().collect {
                    viewBinding.textViewFeedback.text = "${it.text}"
                    viewBinding.nameLastFeedback.text = "${it.id.value?.name}"
                    viewBinding.markFeedback.text = "${it.mark}"
                }
            }
        }
    }


    private fun changeView(text: EditText, btn: ImageView) {
        text.setOnClickListener {
            text.isFocusable
            if (text.isFocusable) {
                btn.isVisible = true
            }
        }

        btn.setOnClickListener {
            text.isFocusable = false
            btn.isVisible = false
            it.hideKeyboard()
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
            adapterLastest?.set(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}