package com.example.appsomefood.BottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.appsomefood.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.appsomefood.databinding.FragmentBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class BottomSheetFragment : BottomSheetDialogFragment() {
    private val viewModelBottom: BottomSheetViewModel by viewModel()
    private val viewBinding: FragmentBottomSheetBinding by viewBinding()


    companion object {
        private const val FOOD = "UUID_FOOD"
        fun getInstance(data: String) = BottomSheetFragment().apply {
            arguments = Bundle().apply {
                putString(FOOD, data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        takeFoodInfo()
        initObserve()
        initView()
    }

    private fun initView() {
        val idFood = arguments?.getString(FOOD)
        val myTimes = arrayOf("15", "20", "25", "30", "35", "40", "45", "50", "55", "60")
        if (idFood != null) {
            viewModelBottom.takeFood(idFood)
        }
        with(viewBinding.timePicker) {
            value = myTimes[0].toInt()
            minValue = 0
            maxValue = myTimes.size - 1
            displayedValues = myTimes
            wrapSelectorWheel = false
            descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        }

        with(viewBinding) {
            volumeMinus.setOnClickListener {
                viewModelBottom.minusSome()
            }

            volumePlus.setOnClickListener {
                viewModelBottom.plusSome()
            }

            hideSheet.setOnClickListener {
                dialog?.cancel()
            }

            acceptOrder.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val minute = timePicker.value.toString()
                    if (idFood != null) {
                        viewModelBottom.number.value.let {
                            viewModelBottom.putInOrder(
                                idFood,
                                myTimes[minute.toInt()],
                                it
                            )
                        }
                    }
                }
                dialog?.cancel()
            }
        }
    }

    private fun initObserve(){
        viewModelBottom.number.onEach {
            viewBinding.volume.text = it.toString()
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }


    private fun takeFoodInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelBottom.food.filterNotNull().collect {
                    with(viewBinding) {
                        Glide.with(imageForOrderBottomSheet.context)
                            .load(it.image)
                            .into(imageForOrderBottomSheet)
                        nameSheetFood.text = "${it.name}"
                    }
                }
            }
        }
    }
}



