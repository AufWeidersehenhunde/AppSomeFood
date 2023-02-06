package com.example.appsomefood.OrdersInWork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.AuthSuccessForCreator.ListCreatorItem
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentOrdersInWorkBinding
import com.example.appsomefood.FeedbackDialog.FeedbackDialogFragment
import com.example.appsomefood.Orders.OrdersModel
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersInWorkFragment : Fragment(R.layout.fragment_orders_in_work) {
    private val viewOrdersWorkListViewModel: OrdersInWorkViewModel by viewModel()
    private val viewBinding: FragmentOrdersInWorkBinding by viewBinding()
    private val itemAdapter = ItemAdapter<ListOrdersInWorkItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeElement()
        initViews()
    }

    private fun initViews(){
        with(viewBinding.recyclerViewOrdersInWork) {
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
                            is Order -> it.order?.let { it1 ->
                                viewOrdersWorkListViewModel.orderDone(
                                    it1.number
                                )
                            }

                            is DoneOrder -> it.order?.idUser?.let { it1 ->
                                FeedbackDialogFragment.getInstance(it.order.number, it1)
                                    .show(childFragmentManager, FeedbackDialogFragment.TAG)
                            }
                        }

                    }
                })
            }
        }
    }
}