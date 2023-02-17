package com.example.appsomefood.profileFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.R
import com.example.appsomefood.databinding.RecyclerViewItemForLastestBinding
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListLastestItem(
    val item: OrdersModel?
) : AbstractBindingItem<RecyclerViewItemForLastestBinding>() {

    override val type: Int
        get() = R.id.identificatorLast


    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): RecyclerViewItemForLastestBinding {
        return RecyclerViewItemForLastestBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: RecyclerViewItemForLastestBinding, payloads: List<Any>) {
        with(binding) {
            nameFoodLast.text = item?.name
            creatorOrClientFood.text = item?.nameCreator
            Glide.with(imageViewFoodLast.context)
                .load(item?.image)
                .into(imageViewFoodLast)
        }
    }
}

