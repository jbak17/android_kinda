package io.bsconsulting.cosc370

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


/**
 * Last adaptor to provide Trackable items for display in settings
 */
class TrackableListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<TrackableListAdapter.TrackableViewHolder>(), AnkoLogger {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var trackables = emptyList<Trackable>() // Cached copy of words

    internal fun setTrackables(trackables: List<Trackable>){
        this.trackables = trackables
        notifyDataSetChanged()
    }

    inner class TrackableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val trackableItemName: TextView = itemView.findViewById(R.id.trackableType)
        val trackableItemFreqency: TextView = itemView.findViewById(R.id.trackableFrequency)
        val trackableItemActive: Switch = itemView.findViewById(R.id.toggleButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackableViewHolder {

        val itemView = inflater.inflate(R.layout.trackable_settings_recyclerview, parent, false)
        return TrackableViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackableViewHolder, position: Int) {

        val current = trackables[position]
        holder.trackableItemName.text = current.type.toString()
        holder.trackableItemFreqency.text = current.frequency.toString()
        holder.trackableItemActive.isEnabled = current.active

        holder.trackableItemActive.setOnClickListener(View.OnClickListener {
            warn(it.toString())
            // create new trackable with updated value, remove old value from list and update
            val newTrackable: List<Trackable> = listOf<Trackable>(current.copy(active = !current.active))
            warn(newTrackable.toString())
            val newList: List<Trackable> = listOf(trackables.filterNot { it.type.equals(current.type) }, newTrackable).flatten()
            setTrackables(newList)

        })


    }

    override fun getItemCount() = trackables.size


}