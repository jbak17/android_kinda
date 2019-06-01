package io.bsconsulting.cosc370.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.bsconsulting.cosc370.R
import io.bsconsulting.cosc370.activities.ChecklistActivity
import org.jetbrains.anko.AnkoLogger

class ChecklistSelectionAdapter internal constructor(context: Context): RecyclerView.Adapter<ChecklistSelectionAdapter.ChecklistTitleViewHolder>(), AnkoLogger {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    val ctx = context;
    val checklists = listOf("Childcare", "Park")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistTitleViewHolder {

        val itemView = inflater.inflate(R.layout.checklist_title, parent, false)

        return ChecklistTitleViewHolder(itemView)
    }

    override fun getItemCount(): Int = checklists.size

    override fun onBindViewHolder(holder: ChecklistTitleViewHolder, position: Int) {
        holder.title.text = checklists.get(position)

        holder.title.setOnClickListener{
            val intent: Intent = Intent(ctx, ChecklistActivity::class.java)
            intent.putExtra(EXTRA_CHECKLIST, checklists.get(position))
            ctx.startActivity(intent)
        }
    }

    class ChecklistTitleViewHolder(val textView: View): RecyclerView.ViewHolder(textView){

        val title: TextView = itemView.findViewById(R.id.list_title)

    }

    companion object {
        const val EXTRA_CHECKLIST = "io.bsconsulting.cosc370.EXTRA_CHECKLIST"
    }


}

