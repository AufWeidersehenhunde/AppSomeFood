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
            initView()
        }
    }

    private fun initView() {
        adapterHome =
            RecyclerViewAdapterForNonCreator({
                    viewListViewModel.putFoodToFavorite(it.idFood)}, {
                    BottomSheetFragment.getInstance(it.idFood)
                        .show(requireActivity().supportFragmentManager, "tag")
            },
                {BottomSheetFragment.getInstance(it.idFood)
                    .show(requireActivity().supportFragmentManager, "tag")})
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