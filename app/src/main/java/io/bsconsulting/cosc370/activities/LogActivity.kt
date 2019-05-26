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
import io.bsconsulting.cosc370.adapters.TrackableListAdapter
import io.bsconsulting.cosc370.model.Trackable
import io.bsconsulting.cosc370.persistence.TrackableViewModel

import kotlinx.android.synthetic.main.activity_log.*
import org.jetbrains.anko.AnkoLogger

class LogActivity : AppCompatActivity() {

    private val trackableViewModel: TrackableViewModel by lazy {
        ViewModelProviders.of(this).get(TrackableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = DateAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // link the recyclerView adapter to database
        // The alltrackables is LiveDate, so SettingsActivity will be notified
        // when the data changes. This is then passed to the adapter.
        // when the list of trackables in the ViewModel changes this is passed to
        // the adapter
        trackableViewModel.allTrackables.observe(this, Observer { trackables ->
            //update cache in adapter
            trackables?.let { adapter.setTrackables(it) }
        })
    }

}

class DateAdapter internal constructor(context: Context)
    : RecyclerView.Adapter<DateAdapter.DateViewHolder>(), AnkoLogger {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val ctx: Context = context
    private var trackables = emptyList<Trackable>()

    internal fun setTrackables(trackables: List<Trackable>){
        this.trackables = trackables.filter { it.active }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateAdapter.DateViewHolder {

        val itemView = inflater.inflate(R.layout.log_component, parent, false)
        return DateViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DateAdapter.DateViewHolder, position: Int) {

        val current = trackables[position]

        holder.name.text = current.lastActivity.first().toString()
    }

    override fun getItemCount(): Int = trackables.size

    inner class DateViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val name: TextView = itemView.findViewById(R.id.dateText)

    }
}


