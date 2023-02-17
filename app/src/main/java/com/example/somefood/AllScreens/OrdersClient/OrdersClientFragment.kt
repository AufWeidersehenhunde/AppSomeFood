package com.example.appsomefood.Orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.AllScreens.DialogCancel.DialogFragmentForCancel
import com.example.appsomefood.R
import com.example.appsomefood.FeedbackDialog.FeedbackDialogFragment
import com.example.appsomefood.databinding.FragmentOrdersClientBinding
import com.example.somefood.Utils.EnumAndSealed.AcceptOrder
import com.example.somefood.Utils.EnumAndSealed.DeleteOrder
import com.example.somefood.Utils.EnumAndSealed.Dialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersClientFragment : Fragment(R.layout.fragment_orders_client) {
    private val viewBinding: FragmentOrdersClientBinding by viewBinding()
    private val viewModelOrders: OrdersClientViewModel by viewModel()
    private val itemAdapter = ItemAdapter<ListOrdersClientItem>()
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
            viewModelOrders.listFoodsForRecycler.filterNotNull().collect { list ->
                itemAdapter.set(list.map { model ->
                    ListOrdersClientItem(model) {
                        when (it) {
                            is DeleteOrder ->
                                DialogFragmentForCancel.getInstance(
                                    it.idOrder
                                ).show(childFragmentManager, DialogFragmentForCancel.TAG)

                            is AcceptOrder -> viewModelOrders.acceptOrder(it.order)

                            is Dialog ->
                                FeedbackDialogFragment.getInstance(
                                    it.idOrder, it.userId
                                ).show(childFragmentManager, FeedbackDialogFragment.TAG)
                            else -> {}
                        }
                    }
                })
            }
        }
    }
}




