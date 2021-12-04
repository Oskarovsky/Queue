package com.oskarro.queue.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.oskarro.queue.R
import com.oskarro.queue.model.ProductDto
import kotlinx.android.synthetic.main.item_row_product.view.*

class ProductRowsAdapter(private val context: Context,
                         private val list: List<ProductDto>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_row_product, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.setBackgroundColor((Color.parseColor(model.stage.color)))
            holder.itemView.tv_row_product_code.text = model.orderNumber
            holder.itemView.tv_row_product_invoice.text = model.invoiceNumber
            holder.itemView.tv_row_product_name.text = model.name
            holder.itemView.tv_row_product_stage.text = model.stage.value

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: ProductDto)
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}
