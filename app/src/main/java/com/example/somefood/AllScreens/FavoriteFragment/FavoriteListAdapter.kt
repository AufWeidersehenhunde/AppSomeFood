package com.example.appsomefood.FavoriteFragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.appsomefood.R
import com.example.appsomefood.databinding.RecyclerItemClientListBinding
import com.example.somefood.Utils.ClickListener.ClickListener
import com.example.somefood.Utils.ClickListener.DeleteFavorite
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListFavoriteItem(
    val item: FavoriteModel, private val onClick: (ClickListener) -> Unit
) : AbstractBindingItem<RecyclerItemClientListBinding>() {

    override val type: Int
        get() = R.id.identificator

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): RecyclerItemClientListBinding {
        return RecyclerItemClientListBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: RecyclerItemClientListBinding, payloads: List<Any>) {
        with(binding) {
            name.text = item.name
            viewBtnAddToFavourite.setColorFilter(Color.RED)
            Glide.with(imageView.context)
                .load(item.image)
                .into(imageView)
            btnAddToFavourite.setOnClickListener {
                onClick(DeleteFavorite(item.idFood))
            }
            btnAdd.isVisible = false
        }
    }
}





