package com.example.appsomefood.FavoriteFragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appsomefood.databinding.RecyclerViewItemBinding

class RecyclerViewAdapterFavorite(private val delFavorite: (FavoriteModel) -> Unit): RecyclerView.Adapter<RecyclerViewAdapterFavorite.MyViewHolder>() {
    var item: List<FavoriteModel?> = listOf()

    fun set(items: List<FavoriteModel>) {
        this.item = items
        notifyDataSetChanged()
    }

    class MyViewHolder(itemBinding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val binding = itemBinding
        fun bind(food: FavoriteModel,
        delFavorite:(FavoriteModel) -> Unit){
            binding.apply {
                viewBtnAddToFavourite.setColorFilter(Color.RED)
                name.text = food.name
                Glide.with(imageView.context)
                    .load(food.image)
                    .into(imageView)
                btnAddToFavourite.setOnClickListener {
                    delFavorite(food)
                }
                btnAdd.visibility = View.INVISIBLE
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        item[position]?.let { holder.bind(it, delFavorite) }

    }

    override fun getItemCount(): Int {
        return item.size
    }



}