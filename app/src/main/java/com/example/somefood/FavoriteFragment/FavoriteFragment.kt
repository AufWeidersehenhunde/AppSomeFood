package com.example.appsomefood.FavoriteFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appsomefood.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.AuthSuccessForNonCreator.ListClientItem
import com.example.appsomefood.databinding.FragmentFavoriteBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
            layoutManager = GridLayoutManager(
                context,
                2
            )
            adapter = fastAdapter
        }
    }

    private fun initObservers(){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFavorite.listFoods.filterNotNull().collect {
                itemAdapter.set(it.map {
                    ListFavoriteItem(it) {
                        when (it) {
                            is DeleteFavorite -> it.idFood?.let { it1 ->
                                viewModelFavorite.delFoodInFavorite(
                                    it1
                                )
                            }
                        }
                    }
                })
            }
        }
    }
}