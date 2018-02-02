package ena.min.lake.sample.activityresult

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.lake.OceanInfix
import ena.min.lake.sample.Streams
import ena.min.lake.sample.appOcean
import kotlinx.android.synthetic.main.activity_result2.*
import java.util.*

class ActivityResultActivity2 : AppCompatActivity(), OceanInfix {

    private val resultItems = listOf(
            ResultItem("John", 33),
            ResultItem("David", 18),
            ResultItem("Lisa", 29),
            ResultItem("Sara", 21),
            ResultItem("Jane", 40),
            ResultItem("Peter", 56),
            ResultItem("Jack", 27)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)

        val resultItem = resultItems[Random().nextInt(resultItems.size)]
        tvNameAndAge.text = "Hi, I'm ${resultItem.name}. I'm ${resultItem.age}"

        Handler().postDelayed({ sendResult(resultItem); finish() }, 2000)
    }

    private fun sendResult(resultItem: ResultItem) {
        resultItem sendTo appOcean via Streams.PERSON_SELECTED
    }
}
