package io.bsconsulting.cosc370.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.bsconsulting.cosc370.R
import io.bsconsulting.cosc370.adapters.ChecklistAdapter
import io.bsconsulting.cosc370.adapters.ChecklistSelectionAdapter.Companion.EXTRA_CHECKLIST
import kotlinx.android.synthetic.main.activity_checklist.*

class ChecklistActivity : AppCompatActivity() {

    lateinit var listName: String;

    val childcareList = listOf("Shoes", "Socks", "Jacket", "Dummy")
    val parkList = listOf("Sand toys", "Sunscreen", "Water", "Dummy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)

        listName = getIntent().getStringExtra(EXTRA_CHECKLIST)

        val listToShow = when (listName){
            "Childcare" -> childcareList
            "Park" -> parkList
            else ->{
                emptyList<String>()}
        }

        btn.setOnClickListener{
            val intent = Intent(this, ChecklistSelectionActivity::class.java)
            startActivity(intent)
        }

        // set up recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = ChecklistAdapter(this, listToShow, btn)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


    }
}
