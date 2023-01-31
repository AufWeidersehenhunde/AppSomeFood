package com.example.appsomefood.AuthAndAuthorize

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentAuthAndRegBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthAndRegFragment : Fragment(R.layout.fragment_auth_and_reg) {
    private val viewModelAuth: AuthAndRegViewModel by viewModel()
    private val viewBinding: FragmentAuthAndRegBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(viewBinding) {
            btnSignIn.setOnClickListener {
                viewModelAuth.routeToAuth()
            }
            btnRegistration.setOnClickListener {
                viewModelAuth.routeToReg()
            }
        }
    }

}