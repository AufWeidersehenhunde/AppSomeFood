package com.example.appsomefood.DialogForTakeOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentDialogForTakeOrderBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogForTakeOrderFragment : DialogFragment() {
    private val viewBindingDialogTake: FragmentDialogForTakeOrderBinding by viewBinding()
    private val viewModelDialog: DialogForTakeOrderViewModel by viewModel()


    companion object {
        const val number = "NUMBER"
        const val nameFood = "name"
        fun getInstance(idOrder: String, name:String) = DialogForTakeOrderFragment().apply {
            arguments = Bundle().apply {
                putString(number, idOrder)
                putString(nameFood, name)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dialog_for_take_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeFood()
    }

    private fun initView() {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val idOrder = arguments?.getString(number)
        val nameFood = arguments?.getString(nameFood)
        if (nameFood != null) {
            viewModelDialog.takeFood(nameFood)
        }

        viewBindingDialogTake.btnCancelTake.setOnClickListener {
            dialog?.cancel()
        }

        viewBindingDialogTake.btnTakeOrder.setOnClickListener {
            if (idOrder != null) {
                viewModelDialog.updateOrder(idOrder)
            }
            dialog?.cancel()
        }
    }
    private fun observeFood(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelDialog.item.filterNotNull().collect {
                with(viewBindingDialogTake) {
                    foodNameForDialog.text = "${it.name}?"
                    textDescriptionFood.text = it.description
                }
            }
        }
    }
}