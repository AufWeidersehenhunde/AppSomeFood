package com.example.appsomefood.OrdersInWork

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentOrdersInWorkBinding
import com.example.appsomefood.FeedbackDialog.FeedbackDialogFragment
import com.example.appsomefood.Orders.OrdersModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersInWorkFragment : Fragment(R.layout.fragment_orders_in_work) {
    private val viewOrdersWorkListViewModel: OrdersInWorkViewModel by viewModel()
    private val viewBinding: FragmentOrdersInWorkBinding by viewBinding()
    private var adapterWorkCreator: AdapterForOrdersInWork? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeElement()
        initView()
    }

    private fun initView(){
        adapterWorkCreator =
            AdapterForOrdersInWork ({
                it.idUser?.let { it1 ->
                        viewOrdersWorkListViewModel.orderDone(it.number)
                }
            }, {uiThreadIsDown(it)})



        with(viewBinding.recyclerViewOrdersInWork) {
            layoutManager = GridLayoutManager(
                context,
                1
            )
            adapter = adapterWorkCreator
        }
    }

    private fun observeElement() {
        viewOrdersWorkListViewModel.listOrdersForRecycler.filterNotNull().onEach {
            adapterWorkCreator?.set(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun uiThreadIsDown(it:OrdersModel){
        it.idUser?.let { it1 ->
            FeedbackDialogFragment.getInstance(it.number, it1)
                .show(childFragmentManager, FeedbackDialogFragment.TAG)
        }
    }
}