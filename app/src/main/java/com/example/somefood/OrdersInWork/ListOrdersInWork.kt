package com.example.appsomefood.OrdersInWork

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.R
import com.example.somefood.ClickListener.ClickListener
import com.example.somefood.ClickListener.DoneOrder
import com.example.somefood.ClickListener.Order
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView

class ListOrdersInWorkItem(
    val item: OrdersModel?, private val Click: (ClickListener) -> Unit
) : AbstractItem<ListOrdersInWorkItem.ViewHolder>() {

    override val type: Int
        get() = R.id.identificatorCreator

    override val layoutRes: Int
        get() = R.layout.recycler_item_creator_list

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)

    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ListOrdersInWorkItem>(view) {
        var name: TextView = view.findViewById(R.id.nameFood)
        var takeOrder: CardView = view.findViewById(R.id.btnDoneOrder)
        var delOrder: CardView = view.findViewById(R.id.btnDelOrder)
        var image: CircleImageView = view.findViewById(R.id.imageViewFood)
        var description: TextView = view.findViewById(R.id.descriptionFood)
        var volume: TextView = view.findViewById(R.id.volumeOrderCreator)
        var minute: TextView = view.findViewById(R.id.timerMinute)
        var wait: TextView = view.findViewById(R.id.wait)

        override fun bindView(item: ListOrdersInWorkItem, payloads: List<Any>) {
            name.text = item.item?.name
            volume.text = item.item?.volume.toString()
            minute.text = item.item?.time
            Glide.with(image.context)
                .load(item.item?.image)
                .into(image)

            takeOrder.setOnClickListener {
                wait.isVisible = true
                item.Click(Order(item.item))
                item.Click(DoneOrder(item.item))
            }
            delOrder.isVisible = false
        }

        override fun unbindView(item: ListOrdersInWorkItem) {
            name.text = null
        }
    }
}
