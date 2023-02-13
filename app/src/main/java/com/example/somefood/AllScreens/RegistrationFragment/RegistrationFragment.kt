package com.example.appsomefood.RegistrationFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.appsomefood.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.AuthFragment.toastAuth
import com.example.appsomefood.databinding.FragmentRegistrationBinding
import com.example.somefood.Services.hideKeyboard
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val viewModelRegistrationFragment: RegistrationViewModel by viewModel()
    private val viewBinding: FragmentRegistrationBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelRegistrationFragment.toast.collect {
                when (it) {
                    toastAuth.PASS -> Toast.makeText(
                        context,
                        "Введите пароль!!!",
                        Toast.LENGTH_SHORT
                    ).show()

                    toastAuth.LOGIN -> Toast.makeText(
                        context,
                        "Введите логин",
                        Toast.LENGTH_SHORT
                    ).show()

                    toastAuth.PASSINVALID -> Toast.makeText(
                        context,
                        "Пароли не совпадают",
                        Toast.LENGTH_SHORT
                    ).show()

                    toastAuth.LOGININVALID -> Toast.makeText(
                        context,
                        "Email is not valid!!!",
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> {}
                }
                viewModelRegistrationFragment.regBoolean.filterNotNull().onEach {
                    if (it) {
                        Toast.makeText(context, "Такой логин уже занят!", Toast.LENGTH_SHORT).show()
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun initViews() {
        with(viewBinding) {
            btnBack.setOnClickListener { view ->
                view.hideKeyboard()
                viewModelRegistrationFragment.goToBack()
            }
            btnAccept.setOnClickListener {
                viewModelRegistrationFragment.checkInput(
                    viewBinding.loginReg.text.toString(),
                    viewBinding.passReg.text.toString(),
                    viewBinding.secondPassReg.text.toString(),
                    false
                )
            }
        }
    }
}
