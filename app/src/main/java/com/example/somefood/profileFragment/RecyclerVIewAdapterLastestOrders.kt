package com.example.appsomefood.profileFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appsomefood.databinding.RecyclerViewItemForLastestBinding
import com.example.appsomefood.Orders.OrdersModel


class RecyclerVIewAdapterLastestOrders:
    RecyclerView.Adapter<RecyclerVIewAdapterLastestOrders.MyViewHolder>() {
    var item: List<OrdersModel?> = emptyList()

    fun set(items: List<OrdersModel>) {
        this.item = items
        notifyDataSetChanged()
    }


    class MyViewHolder(itemBinding: RecyclerViewItemForLastestBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val binding = itemBinding
        fun bind(
            food: OrdersModel
        ) {
            binding.apply {
                val name = food.name
                val image = food.image
                nameFood.text = name
                Glide.with(imageViewFood.context)
                    .load(image)
                    .into(imageViewFood)
                creatorOrClientFood.text = food.nameCreator
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            RecyclerViewItemForLastestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        item[position]?.let { holder.bind(it) }

    }

    override fun getItemCount(): Int {
        return item.size
    }


}