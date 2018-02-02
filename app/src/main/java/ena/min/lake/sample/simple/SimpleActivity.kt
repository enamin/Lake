package ena.min.lake.sample.simple

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.aminenami.jenkinstest.R
import kotlinx.android.synthetic.main.activity_simple.*

class SimpleActivity : AppCompatActivity(), SimpleViewContract {

    val lake = SimpleLake()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        lake.connect(view = this)
    }

    override fun updateTimerText(text: String) {
        tvSimpleTime.text = text
    }

    override fun destroyView() {
        super.destroyView()
        finish()
    }
}
