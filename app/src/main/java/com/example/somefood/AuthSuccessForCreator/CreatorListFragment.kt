package com.example.appsomefood.AuthSuccessForCreator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.R
import com.example.appsomefood.databinding.FragmentCreatorListBinding
import com.example.appsomefood.DialogForTakeOrder.DialogForTakeOrderFragment
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreatorListFragment : Fragment(R.layout.fragment_creator_list) {
    private val viewCreatorListViewModel: CreatorListViewModel by viewModel()
    private val viewBinding: FragmentCreatorListBinding by viewBinding()
    private var adapterHomeCreator: RecyclerViewAdapterForCreator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeElement()
        initViews()
    }

    private fun initViews(){
        adapterHomeCreator =
            RecyclerViewAdapterForCreator {
                    DialogForTakeOrderFragment.getInstance(it.number)
                        .show(childFragmentManager, DialogFragmentForCancel.TAG)
            }

        with(viewBinding.recyclerViewCreator) {
            layoutManager = GridLayoutManager(
                context,
                1
            )
            adapter = adapterHomeCreator
        }
    }

    private fun observeElement() {
        viewCreatorListViewModel.listOrdersForRecycler.filterNotNull().onEach {
            adapterHomeCreator?.set(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}