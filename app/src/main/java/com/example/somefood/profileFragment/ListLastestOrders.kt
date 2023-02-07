package com.example.appsomefood.profileFragment

import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.appsomefood.Orders.OrdersModel
import com.example.appsomefood.R
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView

class ListLastestItem(
    val item: OrdersModel?
) : AbstractItem<ListLastestItem.ViewHolder>() {

    override val type: Int
        get() = R.id.identificatorLast

    override val layoutRes: Int
        get() = R.layout.recycler_view_item_for_lastest

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)

    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ListLastestItem>(view) {
        var nameFood: TextView = view.findViewById(R.id.nameFoodLast)
        var image: CircleImageView = view.findViewById(R.id.imageViewFoodLast)
        var nameCreator: TextView = view.findViewById(R.id.creatorOrClientFood)

        override fun bindView(item: ListLastestItem, payloads: List<Any>) {
            nameFood.text = item.item?.name
            nameCreator.text = item.item?.nameCreator
            Glide.with(image.context)
                .load(item.item?.image)
                .into(image)
        }

        override fun unbindView(item: ListLastestItem) {
            nameFood.text = null
        }
    }
}

