package com.example.appsomefood.OrdersInWork

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.R
import com.example.appsomefood.databinding.RecyclerItemCreatorListBinding
import com.example.somefood.ClickListener.ClickListener
import com.example.somefood.ClickListener.DoneOrder
import com.example.somefood.ClickListener.Order
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class ListOrdersInWorkItem(
    val item: OrdersModel, private val Click: (ClickListener) -> Unit
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

            btnDoneOrder.setOnClickListener {
                wait.isVisible = true
                Click(Order(item))
                Click(DoneOrder(item))
            }
            btnDelOrder.isVisible = false
        }
    }
}

