package io.bsconsulting.cosc370.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import io.bsconsulting.cosc370.R
import kotlinx.android.synthetic.main.activity_add_trackable.*

class AddTrackableActivity : AppCompatActivity() {

    private lateinit var editActivityView: EditText
    private lateinit var editFrequency: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trackable)
        editActivityView = edit_activity
        editFrequency = frequencyInput

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()

            if(TextUtils.isEmpty(editActivityView.text) || TextUtils.isEmpty(editFrequency.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {

                val name = editActivityView.text.toString()
                val frequency = editFrequency.text.toString()
                val active: Boolean = set_active.isChecked
                replyIntent.putExtra(EXTRA_NAME, name)
                replyIntent.putExtra(EXTRA_FREQUENCY, frequency)
                replyIntent.putExtra(EXTRA_ACTIVE, active)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {

        const val EXTRA_NAME = "io.bsconsulting.cosc370.activitylistsql.NAME"
        const val EXTRA_FREQUENCY = "io.bsconsulting.cosc370.activitylistsql.FREQUENCY"
        const val EXTRA_ACTIVE = "io.bsconsulting.cosc370.activitylistsql.ACTIVE"
    }
}
