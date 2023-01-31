package com.example.appsomefood.FeedbackDialog

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentFeedbackDialogBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FeedbackDialogFragment : DialogFragment() {
    private val viewBindingDialog: FragmentFeedbackDialogBinding by viewBinding()
    private val viewModelDialog: FeedbackDialogViewModel by viewModel()
    private var numberRating: Double? = null

    companion object {
        const val TAG = "Dialog"
        const val userId = "USER"
        const val number = "NUMBER"
        fun getInstance(idOrder: String, idUser: String) = FeedbackDialogFragment().apply {
            arguments = Bundle().apply {
                putString(number, idOrder)
                putString(userId, idUser)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        viewModelDialog.checkStatus()
        val idOrder = arguments?.getString(number)
        viewBindingDialog.ratingBarIndicator.setOnRatingBarChangeListener { _, f1, _ ->
            numberRating = f1.toDouble()
        }

        viewBindingDialog.sendFeedback.setOnClickListener {
            numberRating = viewBindingDialog.ratingBarIndicator.rating.toDouble()
            if (numberRating!! < 0.5) {
                Toast.makeText(context, "Minimal mark 0,5", Toast.LENGTH_SHORT).show()
            } else {
                if (idOrder != null) {
                    viewModelDialog.checkFeedback(idOrder,
                        viewBindingDialog.feedback.text.toString(), numberRating!!)
                }
                dialog?.cancel()
            }
        }
    }

}
