package com.oskarro.queue.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oskarro.queue.R
import com.oskarro.queue.model.Product
import kotlinx.android.synthetic.main.item_product.view.*

open class ProductListItemAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener : OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_process, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            holder.itemView.tv_card_name.text = model.name
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener

    }

    interface OnClickListener{
        fun onClick(position : Int)
    }

    private class MyViewHolder(view : View): RecyclerView.ViewHolder(view)

}