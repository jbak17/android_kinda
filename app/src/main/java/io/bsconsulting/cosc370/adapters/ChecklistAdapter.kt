package io.bsconsulting.cosc370.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import io.bsconsulting.cosc370.R
import org.jetbrains.anko.AnkoLogger

class ChecklistAdapter internal constructor(context: Context, checklist: List<String>, btn: Button): RecyclerView.Adapter<ChecklistAdapter.ChecklistItemViewHolder>(), AnkoLogger {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var itemsCompleted = 0

    val button = btn
    val checklists = checklist

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistItemViewHolder {

        val itemView = inflater.inflate(R.layout.checklist_title, parent, false)

        return ChecklistItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChecklistItemViewHolder, position: Int) {
        holder.title.text = checklists.get(position)
        val strikeout = Paint.STRIKE_THRU_TEXT_FLAG
        val flags: Int = holder.title.paintFlags
        holder.title.setOnClickListener{

            when(holder.isComplete){
                true -> {
                    itemsCompleted -=1
                    holder.title.setPaintFlags(flags)
                }
                false -> {
                    itemsCompleted +=1
                    holder.title.setPaintFlags(strikeout)
                }
            }

            holder.isComplete = !holder.isComplete

            when(itemsCompleted == itemCount){
                true -> button.isVisible = true
            }
        }

    }

    override fun getItemCount(): Int = checklists.size

    class ChecklistItemViewHolder(val textView: View): RecyclerView.ViewHolder(textView){

        val title: TextView = itemView.findViewById(R.id.list_title)
        var isComplete: Boolean = false

    }


}

