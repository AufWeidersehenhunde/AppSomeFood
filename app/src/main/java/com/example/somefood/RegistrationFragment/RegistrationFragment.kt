package com.example.appsomefood.RegistrationFragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.appsomefood.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.databinding.FragmentRegistrationBinding
import com.example.appsomefood.DBandProvider.UsersDb
import com.example.somefood.Services.Services
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistrationFragment : Fragment(R.layout.fragment_registration) {
    private val viewModelRegistrationFragment: RegistrationViewModel by viewModel()
    private val viewBinding: FragmentRegistrationBinding by viewBinding()
    private val services = Services()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }


    private suspend fun initObservers(){
        viewModelRegistrationFragment.regBoolean.collect{
            if(it){
                Toast.makeText(context, "Такой логин уже занят!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initViews(){
        with(viewBinding){
            btnBack.setOnClickListener { view->
                services.apply {
                    view.hideKeyboard()
                }
                viewModelRegistrationFragment.goToBack()
            }
            btnAccept.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    if (viewBinding.passReg.text.toString() == "") {
                        Toast.makeText(context, "Введите пароль!!!", Toast.LENGTH_SHORT).show()
                    } else if(!isEmailValid(viewBinding.loginReg.text.toString())){
                        Toast.makeText(context, "Email is not valid!!!", Toast.LENGTH_SHORT).show()
                    }
                    else if (viewBinding.passReg.text.toString() != viewBinding.secondPassReg.text.toString())
                    {
                        Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        viewModelRegistrationFragment.register(viewBinding.loginReg.text.toString(),viewBinding.passReg.text.toString(),false)
                        initObservers()
                    }
                }
            }
        }
    }
    private fun isEmailValid(email:String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        val matcher: Matcher = pattern.matcher(email);
        return matcher.matches()
    }
}
