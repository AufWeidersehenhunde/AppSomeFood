package com.example.appsomefood.AuthSuccessForNonCreator

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appsomefood.R
import com.example.appsomefood.databinding.RecyclerItemClientListBinding
import com.example.somefood.AllScreens.ClientList.Foods
import com.example.somefood.Utils.EnumAndSealed.AddToFavorite
import com.example.somefood.Utils.EnumAndSealed.AddToOrder
import com.example.somefood.Utils.EnumAndSealed.ClickListener
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListClientItem(
    val item: Foods, private val onClick: (ClickListener) -> Unit
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
            if (item.idFoodFavorite!=null){
                viewBtnAddToFavourite.setColorFilter(Color.RED)
            } else{
                viewBtnAddToFavourite.setColorFilter(Color.BLACK)
            }
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


