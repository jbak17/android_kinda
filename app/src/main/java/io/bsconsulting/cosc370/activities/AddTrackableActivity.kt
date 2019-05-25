package io.bsconsulting.cosc370.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import io.bsconsulting.cosc370.R

class AddTrackableActivity : AppCompatActivity() {

    private lateinit var editActivityView: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_trackable)
        editActivityView = findViewById(R.id.edit_activity)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()

            if(TextUtils.isEmpty(editActivityView.text)){
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {

                val word = editActivityView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, word)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {

        const val EXTRA_REPLY = "io.bsconsulting.cosc370.activitylistsql.REPLY"
    }
}
