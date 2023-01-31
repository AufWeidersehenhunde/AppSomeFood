package com.example.appsomefood.Orders

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appsomefood.databinding.RecyclerViewItemFavoriteBinding
import com.example.appsomefood.DBandProvider.FoodDb
import com.example.appsomefood.DBandProvider.Orders

class RecyclerViewAdapterOrders(
    private val delOrder: (OrdersModel) -> Unit,
    private val acceptOrder: (OrdersModel) -> Unit,
    private val dialog: (OrdersModel) -> Unit
) :
    RecyclerView.Adapter<RecyclerViewAdapterOrders.MyViewHolder>() {
    var item: List<OrdersModel?> = emptyList()

    fun set(items: List<OrdersModel>) {
        this.item = items
        notifyDataSetChanged()
    }


    class MyViewHolder(
        itemBinding: RecyclerViewItemFavoriteBinding
    ) :
        RecyclerView.ViewHolder(itemBinding.root) {
        private val binding = itemBinding
        private var time: CountDownTimer? = null

        fun bind(
            food: OrdersModel,
            delOrder: (OrdersModel) -> Unit,
            acceptOrder: (OrdersModel) -> Unit,
            dialog: (OrdersModel) -> Unit
        ) {
            binding.apply {
                val name = food.name
                val image = food.image
                nameFood.text = name
                Glide.with(imageViewFavorite.context)
                    .load(image)
                    .into(imageViewFavorite)
                volumeOrder.text = food.volume.toString()
                timerMinutes.text = food.time


                when (food.status) {
                    Status.FREE -> {
                        statusOrder.text = "Not recruited yet"
                        statusOrder.setTextColor(Color.WHITE)
                        btnDone.isInvisible
                    }
                    Status.WORK -> {
                        statusOrder.text = "In work"
                        statusOrder.setTextColor(Color.RED)
                        btnDone.isInvisible
                    }
                    Status.DONE -> {
                        statusOrder.text = "Done"
                        statusOrder.setTextColor(Color.GREEN)
                        btnDone.isVisible
                        btnDelOrder.isInvisible
                    }
                    else -> {
                        statusOrder.text = "Wait"
                        statusOrder.setTextColor(Color.WHITE)
                        btnDone.isInvisible
                        btnDelOrder.isVisible
                    }
                }
                creatorFood.text = food.nameCreator

                btnDelOrder.setOnClickListener {
                    delOrder(food)
                }
                btnDone.setOnClickListener {
                    acceptOrder(food)
                    dialog(food)
                }
                if (food.status == Status.WORK) {
                    time?.cancel()
                    time = object : CountDownTimer(15000L, 200L) {
                        override fun onTick(p0: Long) {
                            timerMinutes.text = p0.toString()
                        }

                        override fun onFinish() {
                            timerMinutes.text = "0"
                        }
                    }.start()
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            RecyclerViewItemFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        item[position]?.let { holder.bind(it, delOrder, acceptOrder, dialog) }

    }

    override fun getItemCount(): Int {
        return item.size
    }


}

