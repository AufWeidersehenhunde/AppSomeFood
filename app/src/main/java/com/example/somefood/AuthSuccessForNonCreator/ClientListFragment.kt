package com.example.appsomefood.AuthSuccessForNonCreator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appsomefood.databinding.FragmentListBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.BottomSheet.BottomSheetFragment
import com.example.appsomefood.FavoriteFragment.DeleteFavorite
import com.example.appsomefood.R
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class ClientListFragment : Fragment(R.layout.fragment_list) {
    private val viewListViewModel: ClientListViewModel by viewModel()
    private val viewBinding: FragmentListBinding by viewBinding()
    private var adapterHome: RecyclerViewAdapterForNonCreator? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            observeElement()
            initViews()
        }
    }

    private fun initViews() {
        adapterHome =
            RecyclerViewAdapterForNonCreator{
                when(it){
                    is AddToFavorite -> it.idFood?.let { it1 ->
                        viewListViewModel.putFoodToFavorite(
                            it1
                        )
                    }
                    is AddToOrder -> it.idFood?.let { it1 ->
                        BottomSheetFragment.getInstance(it1)
                            .show(requireActivity().supportFragmentManager, "tag")
                    }

                    }
                }

        with(viewBinding.recyclerView) {
            layoutManager = GridLayoutManager(
                context,
                2
            )
            adapter = adapterHome
        }
    }

    private fun observeElement() {
        viewListViewModel.listFoods.filterNotNull().onEach {
            adapterHome?.set(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}

