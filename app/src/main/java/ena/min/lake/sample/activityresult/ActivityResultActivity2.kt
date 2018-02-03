package ena.min.lake.sample.activityresult

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.lake.*
import ena.min.lake.sample.Streams
import ena.min.lake.sample.appOcean
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_result2.*
import java.util.*
import java.util.concurrent.TimeUnit

class ActivityResultActivity2 : AppCompatActivity(), OceanInfix, ActivityResult2ViewContract {

    override fun setSomeTexts(text: String) {
        tvNameAndAge.text = text
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result2)

        ActivityResult2Lake(appOcean).connect(view = this)
    }

    override fun destroyView() {
        super.destroyView()
        finish()
    }
}

interface ActivityResult2ViewContract: ViewContract{
    fun setSomeTexts(text: String)
}

class ActivityResult2Lake(val appOcean: Ocean): EasyLake<NO_MODEL, ActivityResult2ViewContract>() {
    private val resultItems = listOf(
            ResultItem("John", 33),
            ResultItem("David", 18),
            ResultItem("Lisa", 29),
            ResultItem("Sara", 21),
            ResultItem("Jane", 40),
            ResultItem("Peter", 56),
            ResultItem("Jack", 27)
    )

    override fun connect(model: NO_MODEL?, view: ActivityResult2ViewContract?): ActivityResult2Lake {
        super.connect(model, view)

        val resultItem = resultItems[Random().nextInt(resultItems.size)]

        view?.setSomeTexts("Hi, I'm ${resultItem.name}. I'm ${resultItem.age}")

        Observable.just(Unit).delay(2, TimeUnit.SECONDS).subscribe {
            sendResult(resultItem)
            flush()
        }

        return this
    }

    private fun sendResult(resultItem: ResultItem) {
        resultItem sendTo appOcean via Streams.PERSON_SELECTED
    }
}
