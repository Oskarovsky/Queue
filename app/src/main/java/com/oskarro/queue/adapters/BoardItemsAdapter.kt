package com.oskarro.queue.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oskarro.queue.R
import com.oskarro.queue.model.Board
import kotlinx.android.synthetic.main.item_board.view.*

open class BoardItemsAdapter(private val context: Context,
                             private var list: ArrayList<Board> ):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_board, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(R.drawable.ic_black_color_back_24p)
                .centerCrop()
                .into(holder.itemView.iv_board_image)

            holder.itemView.tv_item_board_name.text = model.name
            holder.itemView.tv_item_board_created_by.text = "Created by ${model.createdBy}"

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    fun setOnClickListener(onClickListener : OnClickListener){
        this.onClickListener = onClickListener

    }

    interface OnClickListener {
        fun onClick(position: Int, model: Board)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)


}