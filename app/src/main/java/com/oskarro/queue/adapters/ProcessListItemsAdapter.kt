package com.oskarro.queue.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.oskarro.queue.R
import com.oskarro.queue.activities.ProcessListActivity
import com.oskarro.queue.model.Process
import kotlinx.android.synthetic.main.item_process.view.*

open class ProcessListItemsAdapter(private val context: Context,
                                   private var list: ArrayList<Process>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_process, parent, false)
        val layoutParams = LinearLayout.LayoutParams(
            (parent.width * 0.7).toInt(), LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins((15.toDp().toPx()), 0, (40.toDp()).toPx(), 0)
        view.layoutParams = layoutParams
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            if (position == list.size -1) {
                holder.itemView.tv_add_process_list.visibility = View.VISIBLE
                holder.itemView.ll_process_item.visibility = View.GONE
            } else {
                holder.itemView.tv_add_process_list.visibility = View.GONE
                holder.itemView.ll_process_item.visibility = View.VISIBLE
            }

            holder.itemView.tv_process_list_title.text = model.title
            holder.itemView.tv_add_process_list.setOnClickListener {
                holder.itemView.tv_add_process_list.visibility = View.GONE
                holder.itemView.cv_add_process_list_name.visibility = View.VISIBLE
            }

            holder.itemView.ib_close_list_name.setOnClickListener {
                holder.itemView.tv_add_process_list.visibility = View.VISIBLE
                holder.itemView.cv_add_process_list_name.visibility = View.GONE
            }

            holder.itemView.ib_done_list_name.setOnClickListener {
                val listName = holder.itemView.et_process_list_name.text.toString()

                if (listName.isNotEmpty()) {
                    if (context is ProcessListActivity) {
                        context.createProcessList(listName)
                    }
                } else {
                    Toast.makeText(context, "Please enter list name", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holder.itemView.ib_edit_list_name.setOnClickListener {
            holder.itemView.et_edit_process_list_name.setText(model.title)
            holder.itemView.ll_title_view.visibility = View.GONE
            holder.itemView.cv_edit_process_list_name.visibility = View.VISIBLE
        }

        holder.itemView.ib_close_editable_view.setOnClickListener {
            holder.itemView.ll_title_view.visibility = View.VISIBLE
            holder.itemView.cv_edit_process_list_name.visibility = View.GONE
        }

        holder.itemView.ib_done_edit_list_name.setOnClickListener {
            val listName = holder.itemView.et_edit_process_list_name.text.toString()
            if (listName.isNotEmpty()) {
                if (context is ProcessListActivity) {
                    context.updateProcessList(position, listName, model)
                }
            } else {
                Toast.makeText(context, "Please enter list name", Toast.LENGTH_SHORT).show()
            }
        }

        holder.itemView.ib_delete_list.setOnClickListener {
            alertDialogForDeleteList(position, model.title)
        }
    }

    private fun alertDialogForDeleteList(position : Int, title : String){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Alert!")
        builder.setMessage("Are you sure you want to delete $title?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            if(context is ProcessListActivity){
                context.deleteProcessList(position)
            }
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}