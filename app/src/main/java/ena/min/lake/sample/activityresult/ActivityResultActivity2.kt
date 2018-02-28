package ena.min.lake.sample.activityresult

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_result2.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class ActivityResultActivity2 : AppCompatActivity(), DisposableCan, AllInfixes {
    override val disposables = ArrayList<Disposable?>()
    val lake = ActivityResult2Lake()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)


        lake.STREAM_SET_BIG_TEXT thenDoSafe { tvNameAndAge.text = it }
        lake.STREAM_FINISH thenDoSafe { finish() }
        lake.connect()
    }

    override fun onDestroy() {
        clearCan()
        lake.disconnect()
        super.onDestroy()
    }
}

class ActivityResult2Lake : CloudLake() {

    val STREAM_SET_BIG_TEXT = streamOf<String>()
    val STREAM_FINISH = streamOf<Unit>()

    private val resultItems = listOf(
            ResultItem("John", 33),
            ResultItem("David", 18),
            ResultItem("Lisa", 29),
            ResultItem("Sara", 21),
            ResultItem("Jane", 40),
            ResultItem("Peter", 56),
            ResultItem("Jack", 27)
    )

    override fun connect() {
        super.connect()

        val resultItem = resultItems[Random().nextInt(resultItems.size)]

        val report = "Hi, I'm ${resultItem.name}. I'm ${resultItem.age}"
        report sendTo STREAM_SET_BIG_TEXT

        Observable.just(Unit).delay(2, TimeUnit.SECONDS) thenDoSafe {
            sendResult(resultItem)
            Unit sendTo STREAM_FINISH
        }
    }

    private fun sendResult(resultItem: ResultItem) {
        resultItem sendTo ActivityResult1Lake.STREAM_PERSON_SELECTED
    }
}
