package ena.min.lake.sample

import ena.min.android.lake.CloudLake
import ena.min.android.lake.Stream
import ena.min.lake.sample.activityresult.ActivityResultActivity1
import ena.min.lake.sample.list.ListActivity
import ena.min.lake.sample.simple.SimpleActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/3/18.
 */
class MainLake(private val model: MainModelContract) : CloudLake() {

    val STREAM_START_ACTIVITY = Stream<Class<*>>(cloud, "STREAM_START_ACTIVITY")
    val STREAM_LIST_CLICKS = Stream<MainModelItem>(cloud, "STREAM_LIST_CLICKS")
    val STREAM_SHOW_LIST = Stream<Iterable<MainModelItem>>(cloud, "STREAM_SHOW_LIST")

    override fun connect() {
        super.connect()

        model.accessData() pipeTo STREAM_SHOW_LIST

        STREAM_LIST_CLICKS.open()
                .timestamp()
                .distinctUntilChanged { i1, i2 -> i2.time() - i1.time() < 1000 }
                .map { it.value() }
                .delay(150, TimeUnit.MILLISECONDS)
                .then {
                    it.destClazz sendTo STREAM_START_ACTIVITY
                } can this

    }

}


data class MainModelItem(val name: String, val color: String, val destClazz: Class<*>)


interface MainModelContract {
    fun accessData(): Observable<Iterable<MainModelItem>>
}


class MainModel : MainModelContract {

    override fun accessData(): Observable<Iterable<MainModelItem>> {
        return Observable.just(listOf(
                MainModelItem("Simple Activity", "", SimpleActivity::class.java),
                MainModelItem("Activity Result", "", ActivityResultActivity1::class.java),
                MainModelItem("a List fom Net", "", ListActivity::class.java)
        ))
    }
}

