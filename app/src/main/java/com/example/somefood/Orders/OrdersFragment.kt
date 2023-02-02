package com.example.appsomefood.Orders

import DialogFragmentForCancel
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentOrdersBinding
import com.example.appsomefood.FeedbackDialog.FeedbackDialogFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersFragment : Fragment(R.layout.fragment_orders) {
    private val viewBinding:FragmentOrdersBinding by viewBinding()
    private val viewModelOrders: OrdersViewModel by viewModel()
    private var adapterOrders: RecyclerViewAdapterOrders? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            //onSaveInstanceState()
            observeElement()
            initViews()
        }
    }

    private fun initViews(){
        viewModelOrders.takeOrders()
        adapterOrders =
            RecyclerViewAdapterOrders {
                when(it){
                    is DeleteOrder -> it.idOrder?.let { it1 ->
                        DialogFragmentForCancel.getInstance(
                            it1
                        ).show(childFragmentManager, DialogFragmentForCancel.TAG)
                    }
                    is AcceptOrder -> {
                        it.order?.let { it1 -> viewModelOrders.acceptOrder(it1) }
                    }
                    is Dialog -> it.idOrder?.let { it1 ->
                        it.userId?.let { it2 ->
                            FeedbackDialogFragment.getInstance(
                                it1, it2
                            ).show(childFragmentManager, FeedbackDialogFragment.TAG)
                        }
                    }
                }
            }

        with(viewBinding.recyclerViewOrders) {
            layoutManager = LinearLayoutManager(
                context
            )
            adapter = adapterOrders
        }
    }

    private fun observeElement() {
        viewModelOrders.listFoodsForRecycler.filterNotNull().onEach {
            adapterOrders?.set(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}



