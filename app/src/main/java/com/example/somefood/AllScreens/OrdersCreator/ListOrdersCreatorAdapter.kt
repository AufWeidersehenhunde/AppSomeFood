package com.example.appsomefood.OrdersInWork

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.Orders.Status
import com.example.appsomefood.R
import com.example.appsomefood.databinding.RecyclerItemCreatorListBinding
import com.example.somefood.Utils.EnumAndSealed.ClickListener
import com.example.somefood.Utils.EnumAndSealed.DoneOrder
import com.example.somefood.Utils.EnumAndSealed.Order
import com.mikepenz.fastadapter.binding.AbstractBindingItem


class ListOrdersCreatorItem(
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
            descriptionFood.text = item.ingredients
            clientFood.isVisible = false
            Glide.with(imageViewFood.context)
                .load(item.image)
                .into(imageViewFood)
            if (item.status == Status.DONE) {
                wait.isVisible = true
                descriptionFood.isVisible = false
                btnDoneOrder.isVisible = false
            } else {
                wait.isVisible = false
                descriptionFood.isVisible = true
                btnDoneOrder.isVisible = true
            }
            btnDoneOrder.setOnClickListener {
                Click(Order(item))
                Click(DoneOrder(item))
            }
            btnDelOrder.isVisible = false
        }
    }
}

