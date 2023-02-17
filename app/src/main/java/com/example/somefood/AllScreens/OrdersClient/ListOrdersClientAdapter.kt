package com.example.appsomefood.Orders

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.appsomefood.R
import com.example.appsomefood.databinding.RecyclerViewItemOrdersBinding
import com.example.somefood.Utils.EnumAndSealed.AcceptOrder
import com.example.somefood.Utils.EnumAndSealed.ClickListener
import com.example.somefood.Utils.EnumAndSealed.DeleteOrder
import com.example.somefood.Utils.EnumAndSealed.Dialog
import com.mikepenz.fastadapter.binding.AbstractBindingItem

class ListOrdersClientItem(
    val item: OrdersModel, private val onClick: (ClickListener) -> Unit
) : AbstractBindingItem<RecyclerViewItemOrdersBinding>() {

    override val type: Int
        get() = R.id.identificatorOrders

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): RecyclerViewItemOrdersBinding {
        return RecyclerViewItemOrdersBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: RecyclerViewItemOrdersBinding, payloads: List<Any>) {
        with(binding) {
            nameFood.text = item.name
            volumeOrder.text = item.volume.toString()
            timerMinutes.text = item.time
            descriptionFood.text = item.description
            if (item.nameCreator.isNullOrEmpty()){
                creatorFood.text = "Creator is not founded"
            } else{
                creatorFood.text = item.nameCreator
            }
            Glide.with(imageViewFavorite.context)
                .load(item.image)
                .into(imageViewFavorite)
            when (item.status) {
                Status.FREE -> {
                    statusOrder.text = "Not recruited yet"
                    statusOrder.setTextColor(Color.WHITE)
                    btnDone.isVisible = false
                }
                Status.WORK -> {
                    statusOrder.text = "In work"
                    statusOrder.setTextColor(Color.RED)
                    btnDone.isVisible = false
                    btnDelOrder.isVisible = false
                }
                Status.DONE -> {
                    statusOrder.text = "Done"
                    statusOrder.setTextColor(Color.GREEN)
                    btnDone.isVisible = true
                    btnDelOrder.isVisible = true
                }
                else -> {
                    statusOrder.text = "Wait"
                    statusOrder.setTextColor(Color.WHITE)
                    btnDone.isVisible = false
                    btnDelOrder.isVisible = true
                }
            }

            btnDone.setOnClickListener {
                onClick(AcceptOrder(item))
                onClick(Dialog(item.number, item.idUser))
            }

            btnDelOrder.setOnClickListener {
                onClick(DeleteOrder(item.number))
            }
        }
    }
}

