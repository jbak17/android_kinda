package io.bsconsulting.cosc370.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.bsconsulting.cosc370.R
import io.bsconsulting.cosc370.activities.LogActivity
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
    private val ctx: Context = context
    private var trackables = emptyList<Trackable>()

    internal fun setTrackables(trackables: List<Trackable>){
        this.trackables = trackables.filter { it.active }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashViewHolder {

        val itemView = inflater.inflate(R.layout.dash_component, parent, false)

        return DashViewHolder(itemView)

    }

    // todo When the button is clicked, need to update the associated
    // trackable with a new date item. //
    private fun updateTrackable(holder: DashViewHolder){
        holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        holder.progressBar.setProgress(holder.progressBar.max)
    }

    private fun goToLogActivity(context: Context){
        val intent = Intent(context, LogActivity::class.java)
        startActivity(context, intent, null)
    }

    override fun onBindViewHolder(holder: DashViewHolder, position: Int) {

        val current = trackables[position]

        warn(String.format("Binding to %s", current.type))
        // set value of name
        holder.name.text = current.type
        // set action button
        holder.updateButton.setOnClickListener({
            updateTrackable(holder)
            Snackbar.make(it, String.format("%s successfully updated!", holder.name.text), Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        })
        holder.logButton.setOnClickListener({goToLogActivity(context = this.ctx)})


        // set value of progress bar
        holder.progressBar.max = current.frequency

        holder.progressBar.setProgress(calculateProgress(current.lastActivity.first(), holder.progressBar.max))

        val MINUTES = 1L
        val HOURS = MINUTES * 60L
        val critical: Int = (current.frequency*.15).toInt()
        val warn: Int = (current.frequency*.3).toInt()

        val progressBarTimerTask = fixedRateTimer(name = "Progess bar updater",
            initialDelay = 1000*MINUTES, period = 1000*MINUTES
        ){
            //holder.progressBar.setProgress(calculateProgress(current.lastActivity, holder.progressBar.max), true)
            val currentProgress = holder.progressBar.progress
            holder.progressBar.setProgress(currentProgress-1, true)
            if(currentProgress < critical){
                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            } else if(currentProgress < warn) {
                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            } else {
                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            }
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
        val updateButton: ImageButton = itemView.findViewById(R.id.button)
        val logButton: ImageButton = itemView.findViewById(R.id.logButton)

        init {
            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
            progressBar.setScaleY(4f)
        }
    }

    companion object {
        const val dashListAdapterRequestCode = 1
    }

}