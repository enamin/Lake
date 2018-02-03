package ena.min.lake.sample.activityresult

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.lake.sample.appOcean
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_result1.*

class ActivityResultActivity1 : AppCompatActivity(), ActivityResult1ViewContract {

    override val someButtonClicks = PublishSubject.create<Unit>()
    val lake = ActivityResult1Lake(appOcean)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result1)

        lake.connect(view = this)

        btnStartNextActivity.setOnClickListener { someButtonClicks.onNext(Unit) }
    }

    override fun startAnActivity(clazz: Class<*>) {
        startActivity(Intent(this, clazz))
    }

    override fun updateText(text: String) {
        tvResult.text = text
    }

}

