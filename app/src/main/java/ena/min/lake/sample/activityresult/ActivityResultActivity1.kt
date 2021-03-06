package ena.min.lake.sample.activityresult

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.AllInfixes
import ena.min.android.lake.Bin
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_result1.*

class ActivityResultActivity1 : AppCompatActivity(), Bin, AllInfixes {
    override val disposables = ArrayList<Disposable?>()

    val lake = ActivityResult1Lake()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result1)

        lake.STREAM_START_AN_ACTIVITY thenDo { startAnActivity(it) }

        //This is the part we get the result from the next Activity
        updateText(lake.STREAM_UPDATE_TEXT.memory)
        lake.STREAM_UPDATE_TEXT thenDo { updateText(it) }

        btnStartNextActivity.setOnClickListener {
            Unit sendTo lake.STREAM_BUTTON_CLICKS
        }

        lake.connect()
    }

    private fun startAnActivity(clazz: Class<*>?) {
        clazz ?: return
        startActivity(Intent(this, clazz))
    }

    private fun updateText(text: String?) {
        tvResult.text = text
    }

    override fun onDestroy() {
        lake.disconnect()
        clearBin()
        super.onDestroy()
    }

}

