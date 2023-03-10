package com.example.appsomefood.fragmentContainer

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentChildBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContainerFragment : Fragment(R.layout.fragment_child) {
    private val viewModelContainer: ContainerViewModel by viewModel()
    private val viewBinding: FragmentChildBinding by viewBinding()
    private val navigatorHolder2 by this.inject<NavigatorHolder>()
    private val navigator2 by lazy {
        AppNavigator(
            requireActivity(),
            R.id.host_child,
            childFragmentManager
        )
    }
    private var status: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            viewModelContainer.create()
            takeStatus()
            initView()
        }
    }

    private fun initView() {
        if (viewModelContainer.user == null) {
            viewBinding.bottomNavigationViewHome.visibility = View.INVISIBLE
        }
        with(viewBinding) {
            this.bottomNavigationViewHome.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.btnHome -> {
                        if (!status) {
                            viewModelContainer.goBack()
                        } else {
                            viewModelContainer.goToHomeForCreator()
                        }
                        return@setOnItemSelectedListener true
                    }

                    R.id.btnFavorite -> {
                        viewModelContainer.routeToFavorite()
                        return@setOnItemSelectedListener true
                    }

                    R.id.profile -> {
                        viewModelContainer.routeToProfile()
                        return@setOnItemSelectedListener true
                    }
                    R.id.btnOrder -> {
                        if (!status) {
                            viewModelContainer.routeToOrders()
                        } else {
                            viewModelContainer.routeToOrdersWork()
                        }
                        return@setOnItemSelectedListener true
                    }
                    else -> {
                        return@setOnItemSelectedListener false
                    }
                }
            }
        }
    }

    private fun takeStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelContainer.checkAccount()
            viewModelContainer.statusProfile.filterNotNull().collect {
                status = it.isCreator!!
                viewBinding.bottomNavigationViewHome.menu.findItem(R.id.btnFavorite).isVisible =
                    !status
            }
        }
    }


    override fun onResume() {
        super.onResume()
        navigatorHolder2.removeNavigator()
        navigatorHolder2.setNavigator(navigator2)
    }

    override fun onPause() {
        navigatorHolder2.removeNavigator()
        super.onPause()
    }

}
