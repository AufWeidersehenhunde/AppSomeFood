package com.example.appsomefood.DialogForTakeOrder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentDialogForTakeOrderBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogForTakeOrderFragment : DialogFragment() {
    private val viewBindingDialogTake: FragmentDialogForTakeOrderBinding by viewBinding()
    private val viewModelDialog: DialogForTakeOrderViewModel by viewModel()


    companion object {
        const val number = "NUMBER"
        fun getInstance(idOrder: String) = DialogForTakeOrderFragment().apply {
            arguments = Bundle().apply {
                putString(number, idOrder)
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
    }

    private fun initView() {
        val idOrder = arguments?.getString(number)
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
}