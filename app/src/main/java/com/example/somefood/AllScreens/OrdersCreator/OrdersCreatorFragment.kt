package com.example.appsomefood.OrdersInWork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.FeedbackDialog.FeedbackDialogFragment
import com.example.appsomefood.databinding.FragmentOrdersCreatorBinding
import com.example.somefood.Utils.ClickListener.DoneOrder
import com.example.somefood.Utils.ClickListener.Order
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersCreatorFragment : Fragment(R.layout.fragment_orders_creator) {
    private val viewOrdersWorkListViewModel: OrdersCreatorViewModel by viewModel()
    private val viewBinding: FragmentOrdersCreatorBinding by viewBinding()
    private val itemAdapter = ItemAdapter<ListOrdersInWorkItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeElement()
        initViews()
    }

    private fun initViews() {
        with(viewBinding.recyclerViewOrdersInWork) {
            itemAnimator = null
            layoutManager = LinearLayoutManager(
                context
            )
            adapter = fastAdapter
        }
    }

    private fun observeElement() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewOrdersWorkListViewModel.listOrdersForRecycler.filterNotNull().collect {
                itemAdapter.set(it.map {
                    ListOrdersInWorkItem(it) {
                        when (it) {
                            is Order ->
                                viewOrdersWorkListViewModel.orderDone(
                                    it.order.number
                                )

                            is DoneOrder ->
                                FeedbackDialogFragment.getInstance(it.order.number, it.order.idUser)
                                    .show(childFragmentManager, FeedbackDialogFragment.TAG)

                            else -> {}
                        }
                    }
                })
            }
        }
    }
}