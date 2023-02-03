package com.example.appsomefood.Orders

import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appsomefood.databinding.RecyclerViewItemFavoriteBinding

class RecyclerViewAdapterOrders(
    private val onClick: (click: ClickListenerOrdersUser) -> Unit

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
            food: OrdersModel?,
            onClick: (ClickListenerOrdersUser) -> Unit
        ) {
            binding.apply {
                val name = food?.name
                val image = food?.image
                nameFood.text = name
                Glide.with(imageViewFavorite.context)
                    .load(image)
                    .into(imageViewFavorite)
                volumeOrder.text = food?.volume.toString()
                timerMinutes.text = food?.time


                when (food?.status) {
                    Status.FREE -> {
                        statusOrder.text = "Not recruited yet"
                        statusOrder.setTextColor(Color.WHITE)
                        btnDone.isInvisible = true
                    }
                    Status.WORK -> {
                        statusOrder.text = "In work"
                        statusOrder.setTextColor(Color.RED)
                        btnDone.isInvisible = true
                    }
                    Status.DONE -> {
                        statusOrder.text = "Done"
                        statusOrder.setTextColor(Color.GREEN)
                        btnDone.isVisible = true
                        btnDelOrder.isInvisible = true
                    }
                    else -> {
                        statusOrder.text = "Wait"
                        statusOrder.setTextColor(Color.WHITE)
                        btnDone.isInvisible = true
                        btnDelOrder.isVisible = true
                    }
                }
                creatorFood.text = food?.nameCreator

                btnDelOrder.setOnClickListener {
                    onClick(DeleteOrder(food?.number))
                }
                btnDone.setOnClickListener {
                    onClick(AcceptOrder(food))
                    onClick(Dialog(food?.number, food?.idUser))
                }
                if (food?.status == Status.WORK) {
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
        holder.bind(item[position], onClick)

    }

    override fun getItemCount(): Int {
        return item.size
    }
}

sealed class ClickListenerOrdersUser()

class DeleteOrder(val idOrder: String?):ClickListenerOrdersUser()

class AcceptOrder(val order: OrdersModel?):ClickListenerOrdersUser()

class Dialog(val idOrder: String?, val userId:String?):ClickListenerOrdersUser()