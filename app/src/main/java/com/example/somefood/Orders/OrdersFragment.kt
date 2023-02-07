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
import com.example.somefood.ClickListener.AcceptOrder
import com.example.somefood.ClickListener.DeleteOrder
import com.example.somefood.ClickListener.Dialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersFragment : Fragment(R.layout.fragment_orders) {
    private val viewBinding: FragmentOrdersBinding by viewBinding()
    private val viewModelOrders: OrdersViewModel by viewModel()
    private val itemAdapter = ItemAdapter<ListOrdersItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            observeElement()
            initViews()
        }
    }

    private fun initViews() {
        with(viewBinding.recyclerViewOrders) {
            itemAnimator = null
            layoutManager = LinearLayoutManager(
                context
            )
            adapter = fastAdapter
        }
    }

    private fun observeElement() {
        viewModelOrders.observeOrders()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelOrders.listFoodsForRecycler.filterNotNull().collect {
                itemAdapter.set(it.map {
                    ListOrdersItem(it) {
                        when (it) {
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
                            else -> {}
                        }
                    }
                })
            }
        }
    }
}




