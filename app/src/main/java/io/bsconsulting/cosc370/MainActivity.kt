package io.bsconsulting.cosc370

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val trackableViewModel: TrackableViewModel by lazy {
        ViewModelProviders.of(this).get(TrackableViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Set up recycler view by
            // 1. Get reference from xml
            // 2. Create adapter to fill view with contents
            // 3. Add adapter and layout manager to recyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = TrackableListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // link the recyclerView adapter to database
        trackableViewModel.allTrackables.observe(this, Observer { trackables ->
            //update cache in adapter
            trackables?.let { adapter.setTrackables(it) }
        })

        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTrackableActivity::class.java)
            startActivityForResult(intent, newTrackableActivityRequestCode)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == newTrackableActivityRequestCode && resultCode == Activity.RESULT_OK){
            data?.let {
                val newTrackable = it.getStringExtra(AddTrackableActivity.EXTRA_REPLY)
                val trackable = Trackable(newTrackable)
                trackableViewModel.insert(trackable)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()

        }
    }

    companion object {
        const val newTrackableActivityRequestCode = 1
    }
}


