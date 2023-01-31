package com.example.appsomefood.AuthFragment

import android.content.Context
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentAuthBinding
import com.example.appsomefood.DBandProvider.UsersDb
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthFragment : Fragment(R.layout.fragment_auth) {
    private val viewBinding: FragmentAuthBinding by viewBinding()
    private val viewModelAuth: AuthViewModel by viewModel()
    private var creatorStatus: Boolean = false


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
        viewModelAuth.auth.filterNotNull().onEach {
            if (it) {
                Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Wrong password or login!", Toast.LENGTH_SHORT).show()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initView() {
        with(viewBinding) {
            btnPerson.setOnCheckedChangeListener { _, it ->
                if (it) {
                    creatorStatus = true
                    noncreator.visibility = View.INVISIBLE
                    creator.visibility = View.VISIBLE

                } else {
                    creatorStatus = false
                    creator.visibility = View.INVISIBLE
                    noncreator.visibility = View.VISIBLE

                }
            }

            btnBackToSplesh.setOnClickListener {
                viewModelAuth.routeToBack()
                it.hideKeyboard()
            }

            btnSignin.setOnClickListener {
                if (passAuth.text.toString() == "") {
                    Toast.makeText(context, "Введите пароль!!!", Toast.LENGTH_SHORT).show()
                } else if (loginAuth.text.toString() == "") {
                    Toast.makeText(context, "Введите логин!!!", Toast.LENGTH_SHORT).show()
                } else {
                    val model = UsersDb(
                        login = loginAuth.text.toString(),
                        password = passAuth.text.toString(),
                        isCreator = creatorStatus
                    )
                    viewModelAuth.authentication(model)
                }
            }
        }
    }
}