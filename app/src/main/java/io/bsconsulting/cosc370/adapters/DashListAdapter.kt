package io.bsconsulting.cosc370.adapters

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.math.absoluteValue


class DashListAdapter internal constructor(context: Context, viewModel: TrackableViewModel)
    : RecyclerView.Adapter<DashListAdapter.DashViewHolder>(), AnkoLogger {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val ctx: Context = context
    private val viewModel: TrackableViewModel = viewModel
    private var trackables = emptyList<Trackable>()


    // Color vals
    private val red: Int = ContextCompat.getColor(ctx, R.color.red)
    private val yellow: Int = ContextCompat.getColor(ctx, R.color.yellow)
    private val green: Int = ContextCompat.getColor(ctx, R.color.green)

    private fun pulsate(textView: TextView){
        val frequency = 2000L
        textView.setTextColor(red)

        val fadeIn = AlphaAnimation(0.0f, 1.0f)
        fadeIn.duration = (frequency)
        fadeIn.fillAfter = true

        val fadeOut = AlphaAnimation(1.0f, 0.0f)
        fadeOut.duration = (frequency)
        fadeOut.fillAfter = true

        textView.startAnimation(fadeOut)
        textView.startAnimation(fadeIn)
    }


    internal fun setTrackables(trackables: List<Trackable>){
        this.trackables = trackables.filter { it.active }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashViewHolder {

        val itemView = inflater.inflate(R.layout.dash_component, parent, false)

        return DashViewHolder(itemView)

    }

    override fun onBindViewHolder(dashElementToDisplay: DashViewHolder, position: Int) {

        val current = trackables[position]

        var lastEvent = current.getMostRecentEvent()
        var nextEvent: LocalDateTime = current.getNextEventTime()

        warn(String.format("Binding to %s", current.type))
        // set value of name
        dashElementToDisplay.name.text = current.type

        // set progress bar
        var progressBarTimer = setProgressBarValuesAndTimer(dashElementToDisplay,lastEvent,nextEvent)

        // set action button
        dashElementToDisplay.updateButton.setOnClickListener {

            createNewTrackableEntry(dashElementToDisplay)

            progressBarTimer.cancel()
            lastEvent = LocalDateTime.now()
            nextEvent = lastEvent.plusMinutes(current.frequency)
            progressBarTimer = setProgressBarValuesAndTimer(dashElementToDisplay,lastEvent,nextEvent)

            Snackbar.make(it, String.format("%s successfully updated!", dashElementToDisplay.name.text), Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()
        }

        dashElementToDisplay.logButton.setOnClickListener({goToLogActivity(this.ctx, current.type)})


    }


    private fun goToLogActivity(context: Context, type: String){
        val intent = Intent(context, LogActivity::class.java).putExtra(DashActivity.EXTRA_TRACKABLE, type)
        startActivity(context, intent, null)
    }

    // trackable with a new date item. //
    private fun createNewTrackableEntry(holder: DashViewHolder){

        val trackable: Trackable = trackables.filter { it.type.equals(holder.name.text) }.first()
        val times = trackable.activity
        times.add(LocalDateTime.now())

        viewModel.addTime(trackable.type, times)

        notifyDataSetChanged()

    }


    private fun setProgressBarValuesAndTimer(
        dashViewHolder: DashViewHolder,
        lastEvent: LocalDateTime,
        nextEvent: LocalDateTime
    ): Timer {

        val maxGapBetweenEvents: Double = (Duration.between(lastEvent, nextEvent).toMillis()/1000.0).absoluteValue
        val calculatePercentage: (Int) -> Int = percentage(maxGapBetweenEvents)



        val critical: Int = 15
        val warn: Int = 40

        return fixedRateTimer(name = "Progess bar updater",
            initialDelay = 0, period = 1000
        ){

            // set value of progress bar
            // progress bar has max of 100, and we need an integer representing a % of progress
            val timeToNextEvent: Int = (Duration.between(LocalDateTime.now(), nextEvent).toMillis()/1000).toInt().absoluteValue
            val currentProgress: Int = 100 - calculatePercentage(timeToNextEvent)
            dashViewHolder.progressBar.setProgress(currentProgress, true)

            // format colors of bars
            warn(String.format("Updating progress: \n \t Type: %s \n\t Progress: %d", dashViewHolder.name.text, currentProgress))
            if (currentProgress <= 0){
                dashViewHolder.name.setTextColor(red)
                dashViewHolder.name.visibility = VISIBLE
                this.cancel()
            }
            else if(currentProgress < critical){
                dashViewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(red));
                pulsate(dashViewHolder.name)
            } else if(currentProgress < warn) {
                dashViewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(yellow));
            } else {
                dashViewHolder.progressBar.setProgressTintList(ColorStateList.valueOf(green));
            }
        }

    }

    private fun percentage(max: Double): (Int) -> Int {
        return { actual: Int -> (((max - actual)/max)*100).toInt()}
    }

    override fun getItemCount() = trackables.size

    inner class DashViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.textView)
        val progressBar: ProgressBar= itemView.findViewById(R.id.progressBar)
        val updateButton: ImageButton = itemView.findViewById(R.id.button)
        val logButton: ImageButton = itemView.findViewById(R.id.logButton)

    }


}

