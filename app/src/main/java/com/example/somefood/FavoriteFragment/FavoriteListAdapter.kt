package com.example.appsomefood.FavoriteFragment

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import com.bumptech.glide.Glide
import com.example.appsomefood.R
import com.example.somefood.ClickListener.ClickListener
import com.example.somefood.ClickListener.DeleteFavorite
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import de.hdodenhof.circleimageview.CircleImageView

class ListFavoriteItem(
    val item: FavoriteModel?, private val onClick: (ClickListener) -> Unit
) : AbstractItem<ListFavoriteItem.ViewHolder>() {

    override val type: Int
        get() = R.id.identificator

    override val layoutRes: Int
        get() = R.layout.recycler_item_client_list

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)

    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<ListFavoriteItem>(view) {
        var name: TextView = view.findViewById(R.id.name)
        var favorite: ImageView = view.findViewById(R.id.viewBtnAddToFavourite)
        var image: CircleImageView = view.findViewById(R.id.imageView)
        var add: CardView = view.findViewById(R.id.btnAdd)

        override fun bindView(item: ListFavoriteItem, payloads: List<Any>) {
            name.text = item.item?.name
            favorite.setColorFilter(Color.RED)
            Glide.with(image.context)
                .load(item.item?.image)
                .into(image)
            favorite.setOnClickListener {
                item.onClick(DeleteFavorite(item.item?.idFood))
            }
            add.isInvisible = true
        }

        override fun unbindView(item: ListFavoriteItem) {
            name.text = null
        }
    }
}




