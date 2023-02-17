package com.example.appsomefood.AllScreens.DialogCancel

import com.example.appsomefood.databinding.FragmentDialogForCancelBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.Dialog.DialogViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DialogFragmentForCancel : DialogFragment() {
    private val viewBindingDialog: FragmentDialogForCancelBinding by viewBinding()
    private val viewModelDialog: DialogViewModel by viewModel()

    companion object {
        const val TAG = "Dialog"
        const val number = "NUMBER"
        fun getInstance(idOrder: String) = DialogFragmentForCancel().apply {
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
        return inflater.inflate(R.layout.fragment_dialog_for_cancel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val idOrder = arguments?.getString(number)
        viewBindingDialog.btnCancelDel.setOnClickListener {
            dialog?.cancel()
        }
        viewBindingDialog.btnDelOrder.setOnClickListener {
            if (idOrder != null) {
                viewModelDialog.delOrder(idOrder)
            }
            dialog?.cancel()
        }
    }
}