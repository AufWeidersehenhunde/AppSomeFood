package com.example.appsomefood.AuthFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentAuthBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class AuthFragment : Fragment(R.layout.fragment_auth) {
    private val viewBinding: FragmentAuthBinding by viewBinding()
    private val viewModelAuth: AuthViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelAuth.toast.collect {
                when (it) {
                    toastAuth.PASS -> Toast.makeText(
                        context,
                        "Введите пароль!!!",
                        Toast.LENGTH_SHORT
                    ).show()

                    toastAuth.LOGIN -> Toast.makeText(
                        context,
                        "Введите логин!!!",
                        Toast.LENGTH_SHORT
                    ).show()

                    toastAuth.LOGININVALID -> Toast.makeText(
                        context,
                        "Email is not valid!!!",
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> {}
                }
                viewModelAuth.auth.filterNotNull().onEach {
                    if (it) {
                        Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Wrong password or login!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        }
    }

    private fun initView() {
        with(viewBinding) {
            btnPerson.setOnCheckedChangeListener { _, it ->
                if (it) {
                    noncreator.isVisible = false
                    creator.isVisible = true

                } else {
                    creator.isVisible = false
                    noncreator.isVisible = true

                }
            }

            btnBackToSplesh.setOnClickListener {
                viewModelAuth.routeToBack()
                it.hideKeyboard()
            }

//            viewBinding.loginAuth.addTextChangedListener()

            btnSignin.setOnClickListener {
                viewModelAuth.checkInput(
                    loginAuth.text.toString(),
                    passAuth.text.toString(),
                    btnPerson.isChecked
                )
            }
        }
    }
}

