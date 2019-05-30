package io.bsconsulting.cosc370.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.bsconsulting.cosc370.R
import io.bsconsulting.cosc370.adapters.DashListAdapter
import io.bsconsulting.cosc370.persistence.TrackableViewModel
import kotlinx.android.synthetic.main.activity_dash.*

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class DashActivity : AppCompatActivity(), AnkoLogger {

    private val trackableViewModel: TrackableViewModel by lazy {
        ViewModelProviders.of(this).get(TrackableViewModel::class.java)
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash)
        setSupportActionBar(toolbar)
        warn("Creating DashActivity")

        val databaseHitToInitialise = trackableViewModel.initialiseDB();

        // Set up recycler view by
        // 1. Get reference from xml
        // 2. Create adapter to fill view with contents
        // 3. Add adapter and layout manager to recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = DashListAdapter(this, trackableViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // link the recyclerView adapter to database
        trackableViewModel.allTrackables.observe(this, Observer { trackables ->
            //update cache in adapter
            trackables?.let { adapter.setTrackables(it) }

        })

        fab.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent,
                SettingsActivity.newTrackableActivityRequestCode
            )
        }
    }

    companion object {
        const val EXTRA_TRACKABLE = "io.bsconsulting.cosc370.EXTRA_TRACKABLE_TYPE"

    }

}
