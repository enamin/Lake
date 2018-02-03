package ena.min.lake.sample.activityresult

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.Cloud
import ena.min.android.lake.CloudInfix
import ena.min.android.lake.EasyLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_result2.*
import java.util.*
import java.util.concurrent.TimeUnit

class ActivityResultActivity2 : AppCompatActivity(), CloudInfix {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)

        val lake = ActivityResult2Lake()
        lake.STREAM_SET_BIG_TEXT perform { tvNameAndAge.text = it }
        lake.STREAM_FINISH perform { finish() }
        lake.connect()
    }
}

class ActivityResult2Lake : EasyLake() {

    private val cloud = Cloud()
    val STREAM_SET_BIG_TEXT = Stream<String>(cloud, "STREAM_SET_BIG_TEXT")
    val STREAM_FINISH = Stream<Unit>(cloud, "STREAM_FINISH")

    private val resultItems = listOf(
            ResultItem("John", 33),
            ResultItem("David", 18),
            ResultItem("Lisa", 29),
            ResultItem("Sara", 21),
            ResultItem("Jane", 40),
            ResultItem("Peter", 56),
            ResultItem("Jack", 27)
    )

    override fun connect(): ActivityResult2Lake {
        super.connect()

        val resultItem = resultItems[Random().nextInt(resultItems.size)]

        val report = "Hi, I'm ${resultItem.name}. I'm ${resultItem.age}"
        report sendTo STREAM_SET_BIG_TEXT

        Observable.just(Unit).delay(2, TimeUnit.SECONDS) perform  {
            sendResult(resultItem)
            Unit sendTo STREAM_FINISH
        }

        return this
    }

    private fun sendResult(resultItem: ResultItem) {
        resultItem sendTo ActivityResult1Lake.STREAM_PERSON_SELECTED
    }
}
