package com.example.appsomefood.FavoriteFragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appsomefood.databinding.RecyclerViewItemBinding

class RecyclerViewAdapterFavorite(private val delFavorite: (click: ClickListener) -> Unit): RecyclerView.Adapter<RecyclerViewAdapterFavorite.MyViewHolder>() {
    var item: List<FavoriteModel?> = listOf()

    fun set(items: List<FavoriteModel>) {
        this.item = items
        notifyDataSetChanged()
    }

    class MyViewHolder(itemBinding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val binding = itemBinding
        fun bind(food: FavoriteModel?,
        delFavorite:(ClickListener) -> Unit){
            binding.apply {
                viewBtnAddToFavourite.setColorFilter(Color.RED)
                name.text = food?.name
                Glide.with(imageView.context)
                    .load(food?.image)
                    .into(imageView)
                btnAddToFavourite.setOnClickListener {
                    if (food != null) {
                        delFavorite(DeleteFavorite(food.idFood))
                    }
                }
                btnAdd.isInvisible
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(item[position], delFavorite)

    }

    override fun getItemCount(): Int {
        return item.size
    }
}

sealed class ClickListener()

class DeleteFavorite(val idFood: String?) :ClickListener()


