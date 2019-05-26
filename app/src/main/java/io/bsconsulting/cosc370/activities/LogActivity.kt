package io.bsconsulting.cosc370.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.bsconsulting.cosc370.R
import io.bsconsulting.cosc370.model.Trackable
import io.bsconsulting.cosc370.persistence.TrackableViewModel

import kotlinx.android.synthetic.main.activity_log.*
import org.jetbrains.anko.AnkoLogger
import java.time.LocalDateTime

import java.time.format.DateTimeFormatter


class LogActivity : AppCompatActivity(), AnkoLogger {

    private val trackableViewModel: TrackableViewModel by lazy {
        ViewModelProviders.of(this).get(TrackableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)

        // Type passed from Intent
        val trackableType = getIntent().getStringExtra(DashActivity.EXTRA_TRACKABLE)

        // set up recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = DateAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //set up toolbar
        toolbar.setTitle(String.format("%s log:", trackableType))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        trackableViewModel.allTrackables.observe(this, Observer { trackables ->
            //update cache in adapter
            trackables?.let { adapter.setLoggables(trackables.filter { it.type.equals(trackableType) }.first()) }
        })
    }

}

class DateAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<DateAdapter.DateViewHolder>(), AnkoLogger {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val ctx: Context = context
    private var loggableActivities = emptyList<LocalDateTime>()

    internal fun setLoggables(trackable: Trackable){

        val times: List<LocalDateTime> = trackable.activity.sortedDescending()

        this.loggableActivities = times

        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.DateViewHolder {

        val itemView = inflater.inflate(R.layout.log_component, parent, false)
        return DateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DateAdapter.DateViewHolder, position: Int) {

        val current = loggableActivities[position]

        val formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm")

        val formatDateTime = current.format(formatter)

        holder.name.text = formatDateTime
    }

    override fun getItemCount(): Int = loggableActivities.size

    inner class DateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.dateText)

    }
}


