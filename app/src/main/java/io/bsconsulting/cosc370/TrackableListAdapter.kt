package io.bsconsulting.cosc370

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrackableListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<TrackableListAdapter.TrackableViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var trackables = emptyList<Trackable>() // Cached copy of words

    internal fun setTrackables(trackables: List<Trackable>){
        this.trackables = trackables
        notifyDataSetChanged()
    }

    inner class TrackableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val trackableItemView: TextView = itemView.findViewById(R.id.trackableType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackableViewHolder {

        val itemView = inflater.inflate(R.layout.trackable_settings_recyclerview, parent, false)
        return TrackableViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackableViewHolder, position: Int) {

        val current = trackables[position]
        holder.trackableItemView.text = current.type.toString()

    }

    override fun getItemCount() = trackables.size


}