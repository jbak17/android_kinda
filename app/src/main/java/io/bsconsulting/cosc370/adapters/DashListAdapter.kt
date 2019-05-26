package io.bsconsulting.cosc370.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.bsconsulting.cosc370.R
import io.bsconsulting.cosc370.model.Trackable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.time.Duration
import java.time.LocalDateTime
import kotlin.concurrent.fixedRateTimer
import kotlin.math.absoluteValue

class DashListAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<DashListAdapter.DashViewHolder>(), AnkoLogger {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var trackables = emptyList<Trackable>() // Cached copy of words

    internal fun setTrackables(trackables: List<Trackable>){
        this.trackables = trackables
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashViewHolder {

        val itemView = inflater.inflate(R.layout.dash_component, parent, false)

        return DashViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: DashViewHolder, position: Int) {

        val current = trackables[position]
        warn(String.format("Binding to %s", current.type))
        // set value of name
        holder.name.text = current.type
        // set value of progress bar
        holder.progressBar.max = current.frequency
        holder.progressBar.setProgress(calculateProgress(current.lastActivity, holder.progressBar.max))

        // add listener on button to update
//        holder.updateButton //todo

        val MINUTES = 1L
        val HOURS = MINUTES * 60L

        val progressBarTimerTask = fixedRateTimer(name = "Progess bar updater",
            initialDelay = 1000*MINUTES, period = 1000*MINUTES
        ){
            holder.progressBar.setProgress(calculateProgress(current.lastActivity, holder.progressBar.max), true)
            val currentProgress = holder.progressBar.progress
            //holder.progressBar.setProgress(currentProgress-1, true)
        }

    }

    /**
     * param eventTime: when last event happened
     */
    fun calculateProgress(lastTrackableEventTime: LocalDateTime, progressBarMax: Int): Int {



        val TO_HOURS = 60 // conversion for hours

        val now: LocalDateTime= LocalDateTime.now()

        val durationToEvent: Double = Duration.between(lastTrackableEventTime, now).toMinutes().toDouble().absoluteValue

        warn(durationToEvent/progressBarMax * 100)
        return (durationToEvent/(progressBarMax) * 100).toInt()
    }

    override fun getItemCount() = trackables.size



    inner class DashViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.textView)
        val progressBar: ProgressBar= itemView.findViewById(R.id.progressBar)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)

    }

}