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
import io.bsconsulting.cosc370.activities.DashActivity
import io.bsconsulting.cosc370.activities.LogActivity
import io.bsconsulting.cosc370.model.Trackable
import io.bsconsulting.cosc370.persistence.TrackableViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.time.Duration
import java.time.LocalDateTime
import kotlin.concurrent.fixedRateTimer
import kotlin.math.absoluteValue

class DashListAdapter internal constructor(context: Context, viewModel: TrackableViewModel)
    : RecyclerView.Adapter<DashListAdapter.DashViewHolder>(), AnkoLogger {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val ctx: Context = context
    private val viewModel: TrackableViewModel = viewModel
    private var trackables = emptyList<Trackable>()

    internal fun setTrackables(trackables: List<Trackable>){
        this.trackables = trackables.filter { it.active }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashViewHolder {

        val itemView = inflater.inflate(R.layout.dash_component, parent, false)

        return DashViewHolder(itemView)

    }

    // trackable with a new date item. //
    private fun updateTrackable(holder: DashViewHolder){

        // Update progress bar
        updateProgressBar(holder)

        createNewDateEntry(holder)
    }

    private fun createNewDateEntry(holder: DashViewHolder){
        val trackable: Trackable = trackables.filter { it.type.equals(holder.name.text) }.first()
        val times = trackable.activity
        times.add(LocalDateTime.now())
        viewModel.addTime(trackable.type, times)
        notifyDataSetChanged()

    }


    private fun updateProgressBar(holder: DashViewHolder) {

        holder.progressBar.setProgress(holder.progressBar.max)
        holder.progressBar.setProgressTintList(ColorStateList.valueOf(R.color.colorAccent));
    }

    private fun goToLogActivity(context: Context, type: String){
        val intent = Intent(context, LogActivity::class.java).putExtra(DashActivity.EXTRA_TRACKABLE, type)
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
        holder.logButton.setOnClickListener({goToLogActivity(this.ctx, current.type)})


        // set value of progress bar
        holder.progressBar.max = current.frequency
//        holder.progressBar.setProgress(calculateProgress(current.activity.first(), holder.progressBar.max))
        holder.progressBar.setProgress(calculateProgress(current), true)

        val MINUTES = 60L
        val HOURS = MINUTES * 60L
        val critical: Int = (current.frequency*.15).toInt()
        val warn: Int = (current.frequency*.3).toInt()

        val progressBarTimerTask = fixedRateTimer(name = "Progess bar updater",
            initialDelay = 1000*MINUTES, period = 1000*MINUTES
        ){
            //holder.progressBar.setProgress(calculateProgress(current.activity, holder.progressBar.max), true)
            val currentProgress = calculateProgress(current)
            holder.progressBar.setProgress(currentProgress, true)
            if(currentProgress < critical){
                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
            } else if(currentProgress < warn) {
                holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
            } else {
                holder.progressBar.setProgressTintList(ColorStateList.valueOf(R.color.colorAccent));
            }
        }

    }

    /**
     * param eventTime: when last event happened
     */
    fun calculateProgress(trackable: Trackable): Int {

        val then = trackable.activity.sortedDescending().first();
        val now: LocalDateTime= LocalDateTime.now()

        val durationToEvent: Double = Duration.between(then, now).toMinutes().toDouble().absoluteValue

        warn(String.format("%s: %d", trackable.type, (trackable.frequency - durationToEvent).toInt()))
        return (trackable.frequency - durationToEvent).toInt()
    }

    override fun getItemCount() = trackables.size

    inner class DashViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.textView)
        val progressBar: ProgressBar= itemView.findViewById(R.id.progressBar)
        val updateButton: ImageButton = itemView.findViewById(R.id.button)
        val logButton: ImageButton = itemView.findViewById(R.id.logButton)

        init {
//            progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
//            progressBar.setProgress(progressBar.max)
            progressBar.setScaleY(3f)
        }
    }

    companion object {
        const val dashListAdapterRequestCode = 1
    }

}