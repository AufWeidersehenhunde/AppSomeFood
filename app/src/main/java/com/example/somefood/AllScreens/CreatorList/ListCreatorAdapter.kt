package com.example.appsomefood.AuthSuccessForCreator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.R
import com.example.appsomefood.databinding.RecyclerItemCreatorListBinding
import com.example.somefood.Utils.ClickListener.ClickListener
import com.example.somefood.Utils.ClickListener.TakeOrder
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListCreatorItem(
    val item: OrdersModel, private val onClick: (ClickListener) -> Unit
) : AbstractBindingItem<RecyclerItemCreatorListBinding>() {

    override val type: Int
        get() = R.id.identificatorCreator


    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): RecyclerItemCreatorListBinding {
        return RecyclerItemCreatorListBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: RecyclerItemCreatorListBinding, payloads: List<Any>) {
        with(binding) {
            nameFood.text = item.name
            volumeOrderCreator.text = item.volume.toString()
            timerMinute.text = item.time
            Glide.with(imageViewFood.context)
                .load(item.image)
                .into(imageViewFood)
            descriptionFood.text = item.ingredients
            btnDoneOrder.setOnClickListener {
                onClick(TakeOrder(item))
            }
            btnDelOrder.isVisible = false
        }
    }
}

