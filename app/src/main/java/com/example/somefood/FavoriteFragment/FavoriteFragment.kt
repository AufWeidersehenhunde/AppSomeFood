package com.example.appsomefood.FavoriteFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appsomefood.R
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.appsomefood.databinding.FragmentFavoriteBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    private val viewBinding: FragmentFavoriteBinding by viewBinding()
    private val viewModelFavorite: FavoriteViewModel by viewModel()
    private var adapterFavorite: RecyclerViewAdapterFavorite? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeElement()
        initView()
    }

    private fun initView() {
        this.adapterFavorite =
            RecyclerViewAdapterFavorite {
                when(it){
                    is DeleteFavorite -> it.idFood?.let { it1 ->
                        viewModelFavorite.delFoodInFavorite(
                            it1
                        )
                    }
                }
              }
        with(viewBinding.recyclerViewFavorite) {
            layoutManager = GridLayoutManager(
                context,
                2
            )
            adapter = adapterFavorite
        }
    }

    private fun observeElement() {
        viewModelFavorite.listFoods.filterNotNull().onEach {
            adapterFavorite?.set(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}