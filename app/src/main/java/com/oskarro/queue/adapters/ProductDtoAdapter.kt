package com.oskarro.queue.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oskarro.queue.R
import com.oskarro.queue.model.ProductDto
import kotlinx.android.synthetic.main.activity_product_row_details.view.*

class ProductDtoAdapter(private val context: Context,
                        private val product: ProductDto)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_product_row_details, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            holder.itemView.tv_row_product_dto_code.text = product.orderNumber
            holder.itemView.tv_row_product_dto_name.text = product.name
            holder.itemView.tv_row_product_dto_stage.text = product.stage

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, product)
                }
            }
        }
    }

    override fun getItemCount(): Int = 1

    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ProductDto)
    }
    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}