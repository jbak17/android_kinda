package io.bsconsulting.cosc370.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoLogger

class DashListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<DashListAdapter.DashViewHolder>(), AnkoLogger {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: DashViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class DashViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

}