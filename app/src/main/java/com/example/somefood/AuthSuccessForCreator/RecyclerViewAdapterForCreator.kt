package com.example.appsomefood.AuthSuccessForCreator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appsomefood.databinding.RecyclerItemCreatorListBinding
import com.example.appsomefood.Orders.OrdersModel

class RecyclerViewAdapterForCreator (private val forOrder:(OrdersModel)->Unit): RecyclerView.Adapter<RecyclerViewAdapterForCreator.MyViewHolder>() {
    var item: List<OrdersModel> = emptyList()

    fun set(items: List<OrdersModel>) {
        this.item = items
        notifyDataSetChanged()
    }


    class MyViewHolder(itemBinding: RecyclerItemCreatorListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val binding = itemBinding
        fun bind(order: OrdersModel,
                 forOrder:(OrdersModel)->Unit){
            binding.apply {
                nameFood.text = order.name
                Glide.with(imageViewFood.context)
                    .load(order.image)
                    .into(imageViewFood)
                timerMinute.text = order.time
                volumeOrderCreator.text = order.volume.toString()
                btnDoneOrder.setOnClickListener {
                    forOrder(order)
                }
                btnDelOrder.visibility = View.INVISIBLE
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            RecyclerItemCreatorListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        item[position].let { holder.bind(it, forOrder) }
    }

    override fun getItemCount(): Int {
        return this.item.size
    }
}