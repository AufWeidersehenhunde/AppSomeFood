package com.example.appsomefood.AuthSuccessForNonCreator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appsomefood.databinding.FragmentListBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.BottomSheet.BottomSheetFragment
import com.example.appsomefood.R
import com.example.somefood.ClickListener.AddToFavorite
import com.example.somefood.ClickListener.AddToOrder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class ClientListFragment : Fragment(R.layout.fragment_list) {
    private val viewListViewModel: ClientListViewModel by viewModel()
    private val viewBinding: FragmentListBinding by viewBinding()
    private val itemAdapter = ItemAdapter<ListClientItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            initViews()
            initObservers()
        }
    }

    private fun initViews() {
        with(viewBinding.recyclerView) {
            itemAnimator = null
            layoutManager = GridLayoutManager(
                context,
                2
            )
            adapter = fastAdapter
        }
    }

    private fun initObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewListViewModel.listFoods.collect {
                itemAdapter.set(it.map {
                    ListClientItem(it) {
                        when (it) {
                            is AddToFavorite -> it.idFood?.let { it1 ->
                                viewListViewModel.putFoodToFavorite(
                                    it1
                                )
                            }
                            is AddToOrder -> it.idFood?.let { it1 ->
                                BottomSheetFragment.getInstance(
                                    it1
                                ).show(requireActivity().supportFragmentManager, "tag")
                            }
                            else -> {}
                        }

                    }
                })
            }
        }
    }
}


