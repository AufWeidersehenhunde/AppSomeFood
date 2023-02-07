package com.example.appsomefood.Orders

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.appsomefood.R
import com.example.somefood.ClickListener.AcceptOrder
import com.example.somefood.ClickListener.ClickListener
import com.example.somefood.ClickListener.DeleteOrder
import com.example.somefood.ClickListener.Dialog
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView

class ListOrdersItem(
    val item: OrdersModel?, private val onClick:(ClickListener)->Unit
) : AbstractItem<ListOrdersItem.ViewHolder>() {

    override val type: Int
        get() = R.id.identificatorOrders

    override val layoutRes: Int
        get() = R.layout.recycler_view_item_orders

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)

    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ListOrdersItem>(view) {
        var name: TextView = view.findViewById(R.id.nameFood)
        var image: CircleImageView = view.findViewById(R.id.imageViewFavorite)
        var description: TextView = view.findViewById(R.id.descriptionFood)
        var accept: CardView = view.findViewById(R.id.btnDone)
        var cancel: CardView = view.findViewById(R.id.btnDelOrder)
        var volume: TextView = view.findViewById(R.id.volumeOrder)
        var minute: TextView = view.findViewById(R.id.timerMinutes)
        var creator: TextView = view.findViewById(R.id.creatorFood)
        var status: TextView = view.findViewById(R.id.statusOrder)

        override fun bindView(item: ListOrdersItem, payloads: List<Any>) {
            name.text = item.item?.name
            volume.text = item.item?.volume.toString()
            minute.text = item.item?.time
            creator.text = item.item?.nameCreator
            Glide.with(image.context)
                .load(item.item?.image)
                .into(image)
            when (item.item?.status) {
                Status.FREE -> {
                    status.text = "Not recruited yet"
                    status.setTextColor(Color.WHITE)
                    accept.isInvisible = true
                }
                Status.WORK -> {
                    status.text = "In work"
                    status.setTextColor(Color.RED)
                    accept.isInvisible = true
                    cancel.isInvisible = true
                }
                Status.DONE -> {
                    status.text = "Done"
                    status.setTextColor(Color.GREEN)
                    accept.isVisible = true
                    cancel.isInvisible = true
                }
                else -> {
                    status.text = "Wait"
                    status.setTextColor(Color.WHITE)
                    accept.isInvisible = true
                    cancel.isVisible = true
                }
            }
            accept.setOnClickListener {
                item.onClick(AcceptOrder(item.item))
                item.onClick(Dialog(item.item?.number, item.item?.idUser))
            }

            cancel.setOnClickListener {
                item.onClick(DeleteOrder(item.item?.number))
            }
        }

        override fun unbindView(item: ListOrdersItem) {
            name.text = null
            Glide.with(image.context)
                .load(R.drawable.aheg)
                .into(image)
        }
    }
}
