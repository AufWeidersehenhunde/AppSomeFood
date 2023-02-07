package com.example.appsomefood.AuthSuccessForNonCreator

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.R
import com.example.appsomefood.databinding.RecyclerItemClientListBinding
import com.example.somefood.ClickListener.AddToFavorite
import com.example.somefood.ClickListener.AddToOrder
import com.example.somefood.ClickListener.ClickListener
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListClientItem(
    val item: FoodDb, private val onClick: (ClickListener) -> Unit
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
            Glide.with(imageView.context)
                .load(item.image)
                .into(imageView)
            btnAddToFavourite.setOnClickListener {
                onClick(AddToFavorite(item.idFood))
            }
            btnAdd.setOnClickListener {
                onClick(AddToOrder(item.idFood))
            }
            cardViewFoodBody.setOnClickListener {
                onClick(AddToOrder(item.idFood))
            }
        }
    }
}


