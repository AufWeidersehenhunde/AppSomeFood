package com.example.appsomefood.AuthSuccessForCreator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentCreatorListBinding
import com.example.appsomefood.DialogForTakeOrder.DialogForTakeOrderFragment
import com.example.appsomefood.AllScreens.DialogCancel.DialogFragmentForCancel
import com.example.somefood.Utils.EnumAndSealed.TakeOrder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatorListFragment : Fragment(R.layout.fragment_creator_list) {
    private val viewCreatorListViewModel: CreatorListViewModel by viewModel()
    private val viewBinding: FragmentCreatorListBinding by viewBinding()
    private val itemAdapter = ItemAdapter<ListCreatorItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    private fun initViews() {
        with(viewBinding.recyclerViewCreator) {
            layoutManager = LinearLayoutManager(
                context
            )
            adapter = fastAdapter
        }
    }

    private fun initObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewCreatorListViewModel.listOrdersForRecycler.filterNotNull().collect { orders ->
                itemAdapter.set(orders.map {
                    ListCreatorItem(it) { list ->
                        when (list) {
                            is TakeOrder ->
                                DialogForTakeOrderFragment.getInstance(list.item.number, list.item.name)
                                    .show(childFragmentManager, DialogFragmentForCancel.TAG)
                            else -> {}
                        }
                    }
                })
            }
        }
    }
}
