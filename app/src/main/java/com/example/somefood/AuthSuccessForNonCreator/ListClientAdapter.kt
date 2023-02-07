package com.example.appsomefood.AuthSuccessForNonCreator

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.R
import com.example.somefood.ClickListener.AddToFavorite
import com.example.somefood.ClickListener.AddToOrder
import com.example.somefood.ClickListener.ClickListener
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView

class ListClientItem(
    val item: FoodDb?, private val onClick: (ClickListener) -> Unit
) : AbstractItem<ListClientItem.ViewHolder>() {

    override val type: Int
        get() = R.id.identificator

    override val layoutRes: Int
        get() = R.layout.recycler_item_client_list

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)

    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ListClientItem>(view) {
        var name: TextView = view.findViewById(R.id.name)
        var favorite: ImageView = view.findViewById(R.id.viewBtnAddToFavourite)
        var image: CircleImageView = view.findViewById(R.id.imageView)
        var add: ImageView = view.findViewById(R.id.viewBtnAddToOrder)
        var body: CardView = view.findViewById(R.id.cardViewFoodBody)

        override fun bindView(item: ListClientItem, payloads: List<Any>) {
            name.text = item.item?.name
            Glide.with(image.context)
                .load(item.item?.image)
                .into(image)
            favorite.setOnClickListener {
                item.onClick(AddToFavorite(item.item?.idFood))
            }
            add.setOnClickListener {
                item.onClick(AddToOrder(item.item?.idFood))
            }
            body.setOnClickListener {
                item.onClick(AddToOrder(item.item?.idFood))
            }
        }

        override fun unbindView(item: ListClientItem) {
            name.text = null
        }
    }
}

