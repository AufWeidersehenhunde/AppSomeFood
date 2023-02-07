package com.example.appsomefood.AuthSuccessForCreator

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.R
import com.example.somefood.ClickListener.ClickListener
import com.example.somefood.ClickListener.TakeOrder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView

class ListCreatorItem(
    val item: OrdersModel?, private val onClick:(ClickListener)->Unit
) : AbstractItem<ListCreatorItem.ViewHolder>() {

    override val type: Int
        get() = R.id.identificatorCreator

    override val layoutRes: Int
        get() = R.layout.recycler_item_creator_list

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)

    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ListCreatorItem>(view) {
        var name: TextView = view.findViewById(R.id.nameFood)
        var takeOrder: CardView = view.findViewById(R.id.btnDoneOrder)
        var image: CircleImageView = view.findViewById(R.id.imageViewFood)
        var description:TextView = view.findViewById(R.id.descriptionFood)
        var volume: TextView = view.findViewById(R.id.volumeOrderCreator)
        var minute: TextView = view.findViewById(R.id.timerMinute)

        override fun bindView(item: ListCreatorItem, payloads: List<Any>) {
            name.text = item.item?.name
            volume.text = item.item?.volume.toString()
            minute.text = item.item?.time
            Glide.with(image.context)
                .load(item.item?.image)
                .into(image)

            takeOrder.setOnClickListener {
                item.onClick(TakeOrder(item.item?.number))
            }
        }

        override fun unbindView(item: ListCreatorItem) {
            name.text = null
        }
    }
}
