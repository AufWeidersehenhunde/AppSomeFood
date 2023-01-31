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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
                    noncreator.isInvisible
                    creator.isVisible

                } else {
                    creator.isInvisible
                    noncreator.isVisible

                }
            }

            btnBackToSplesh.setOnClickListener {
                viewModelAuth.routeToBack()
                it.hideKeyboard()
            }

            btnSignin.setOnClickListener {
                if (passAuth.text.toString().isEmpty()) {
                    Toast.makeText(context, "Введите пароль!!!", Toast.LENGTH_SHORT).show()
                } else if (loginAuth.text.toString().isEmpty()) {
                    Toast.makeText(context, "Введите логин!!!", Toast.LENGTH_SHORT).show()
                } else if(!isEmailValid(loginAuth.text.toString())){
                    Toast.makeText(context, "Email is not valid!!!", Toast.LENGTH_SHORT).show()
                }else {
                    viewModelAuth.authentication(loginAuth.text.toString(),passAuth.text.toString(),btnPerson.isChecked)
                }
            }
        }
    }
    private fun isEmailValid(email:String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        val pattern:Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        val matcher:Matcher = pattern.matcher(email);
        return matcher.matches()
    }
}

