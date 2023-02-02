package com.example.appsomefood.AuthSuccessForNonCreator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appsomefood.databinding.RecyclerViewItemBinding
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.FavoriteFragment.ClickListener

class RecyclerViewAdapterForNonCreator(
    private val listClient: (click: ClickListenerForList) -> Unit
) : RecyclerView.Adapter<RecyclerViewAdapterForNonCreator.MyViewHolder>() {
    var item: List<FoodDb> = listOf()

    fun set(items: List<FoodDb>) {
        this.item = items
        notifyDataSetChanged()
    }

    class MyViewHolder(itemBinding: RecyclerViewItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val binding = itemBinding
        fun bind(
            food: FoodDb,
           listClient: (click: ClickListenerForList) -> Unit
        ) {
            binding.apply {
                name.text = food.name
                Glide.with(imageView.context)
                    .load(food.image)
                    .into(imageView)
                btnAddToFavourite.setOnClickListener {
                    listClient(AddToFavorite(food.idFood))
                }
                btnAdd.setOnClickListener {
                    listClient(AddToOrder(food.idFood))
                }
                imageView.setOnClickListener {
                    listClient(AddToOrder(food.idFood))
                }
                cardViewFoodBody.setOnClickListener {
                    listClient(AddToOrder(food.idFood))
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(item[position], listClient)
    }

    override fun getItemCount(): Int {
        return item.size
    }
}

sealed class ClickListenerForList()

class AddToFavorite(val idFood: String?): ClickListenerForList()

class AddToOrder(val idFood: String?): ClickListenerForList()