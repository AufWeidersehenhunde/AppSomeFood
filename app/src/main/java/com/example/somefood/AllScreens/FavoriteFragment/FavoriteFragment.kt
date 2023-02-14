package com.example.appsomefood.FavoriteFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appsomefood.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.BottomSheet.BottomSheetFragment
import com.example.appsomefood.databinding.FragmentFavoriteBinding
import com.example.somefood.Utils.ClickListener.AddToOrder
import com.example.somefood.Utils.ClickListener.DeleteFavorite
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private val viewBinding: FragmentFavoriteBinding by viewBinding()
    private val viewModelFavorite: FavoriteViewModel by viewModel()
    private val itemAdapter = ItemAdapter<ListFavoriteItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initObservers()
    }

    private fun initView() {
        with(viewBinding.recyclerViewFavorite) {
            itemAnimator = null
            layoutManager = GridLayoutManager(
                context,
                2
            )
            adapter = fastAdapter
        }
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFavorite.listFoods.filterNotNull().collect { list ->
                itemAdapter.set(list.map { model ->
                    ListFavoriteItem(model) {
                        when (it) {
                            is DeleteFavorite ->
                                    viewModelFavorite.delFoodInFavorite(
                                        it.idFood
                                    )
                            is AddToOrder ->
                                BottomSheetFragment.getInstance(
                                    it.idFood
                                ).show(requireActivity().supportFragmentManager, "tag")

                            else -> {}
                        }
                    }
                })
            }
        }
    }
}