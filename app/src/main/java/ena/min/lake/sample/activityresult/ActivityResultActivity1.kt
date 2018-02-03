package ena.min.lake.sample.activityresult

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.CloudInfix
import kotlinx.android.synthetic.main.activity_result1.*

class ActivityResultActivity1 : AppCompatActivity(), CloudInfix {

    val lake = ActivityResult1Lake()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result1)

        lake.STREAM_START_AN_ACTIVITY perform { startAnActivity(it) }
        lake.STREAM_UPDATE_TEXT perform { updateText(it) }

        btnStartNextActivity.setOnClickListener {
            Unit sendTo lake.STREAM_BUTTON_CLICKS
        }

        lake.connect()
    }

    fun startAnActivity(clazz: Class<*>?) {
        clazz ?: return
        startActivity(Intent(this, clazz))
    }

    fun updateText(text: String?) {
        tvResult.text = text
    }

}

