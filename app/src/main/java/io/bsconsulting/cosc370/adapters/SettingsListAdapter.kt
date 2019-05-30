package io.bsconsulting.cosc370.adapters

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.bsconsulting.cosc370.R
import io.bsconsulting.cosc370.model.Trackable
import io.bsconsulting.cosc370.persistence.TrackableViewModel
import kotlinx.android.synthetic.main.dash_component.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


/**
 * Last adaptor to provide Trackable items for display in settings
 */
class SettingsListAdapter internal constructor(context: Context, viewModel: TrackableViewModel)
    : RecyclerView.Adapter<SettingsListAdapter.TrackableViewHolder>(), AnkoLogger {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var trackables = emptyList<Trackable>() // Cached copy of words
    private var viewModel = viewModel

    internal fun setTrackables(trackables: List<Trackable>){
        this.trackables = trackables
        notifyDataSetChanged()
    }

    inner class TrackableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val trackableItemName: TextView = itemView.findViewById(R.id.trackableType)
        val trackableItemFreqency: TextView = itemView.findViewById(R.id.trackableFrequency)
        val trackableItemActive: Switch = itemView.findViewById(R.id.toggleButton)

        val typeName: TextView = itemView.findViewById(R.id.textViewType)
        val typeFreq: TextView = itemView.findViewById(R.id.textViewFrequency)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackableViewHolder {

        val itemView = inflater.inflate(R.layout.settings_component, parent, false)

        return TrackableViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TrackableViewHolder, position: Int) {

        val current = trackables[position]

        holder.trackableItemName.text = current.type
        holder.trackableItemFreqency.text = current.frequency.toString()
        holder.trackableItemActive.isChecked = current.active

        updateTypefaceOnTextviews(current.active, holder)

        holder.trackableItemActive.setOnClickListener(View.OnClickListener {
            warn(it.toString())

            // update model locally
            viewModel.toggle(current.type, !current.active)

            // update appearance
            updateTypefaceOnTextviews(!current.active, holder)

            // push changes to persistence layer
            notifyDataSetChanged()

        })


    }

    override fun getItemCount() = trackables.size

    // If active then bold typeface, otherwise normal
    fun updateTypefaceOnTextviews(active: Boolean, settingsItem: TrackableViewHolder){
        val big = 18f
        val small = 14f
        if (active) {
            settingsItem.trackableItemName.setTypeface(settingsItem.trackableItemName.getTypeface(), Typeface.BOLD)
            settingsItem.trackableItemFreqency.setTypeface(settingsItem.trackableItemFreqency.getTypeface(), Typeface.BOLD)
            settingsItem.typeName.setTypeface(settingsItem.trackableItemFreqency.getTypeface(), Typeface.BOLD)
            settingsItem.typeFreq.setTypeface(settingsItem.trackableItemFreqency.getTypeface(), Typeface.BOLD)
            settingsItem.typeName.setTextSize(TypedValue.COMPLEX_UNIT_SP, big)
            settingsItem.trackableItemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, big)

        } else {
            settingsItem.trackableItemName.setTypeface(settingsItem.trackableItemName.getTypeface(), Typeface.NORMAL)
            settingsItem.trackableItemFreqency.setTypeface(settingsItem.trackableItemFreqency.getTypeface(), Typeface.NORMAL)
            settingsItem.typeName.setTypeface(settingsItem.trackableItemFreqency.getTypeface(), Typeface.NORMAL)
            settingsItem.typeFreq.setTypeface(settingsItem.trackableItemFreqency.getTypeface(), Typeface.NORMAL)
            settingsItem.typeName.setTextSize(TypedValue.COMPLEX_UNIT_SP, small)
            settingsItem.trackableItemName.setTextSize(TypedValue.COMPLEX_UNIT_SP, small)
        }
    }


}