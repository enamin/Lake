package ena.min.lake.sample

import ena.min.android.lake.Cloud
import ena.min.android.lake.CloudLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/3/18.
 */
class MainLake(val model: MainModelContract) : CloudLake() {

    val STREAM_START_ACTIVITY = Stream<Class<*>>(cloud, "STREAM_START_ACTIVITY")
    val STREAM_LIST_CLICKS = Stream<MainModel.Item>(cloud, "STREAM_LIST_CLICKS")
    val STREAM_SHOW_LIST = Stream<Iterable<MainModel.Item>>(cloud, "STREAM_SHOW_LIST")

    override fun connect() {
        super.connect()

        model.accessData() pipeTo STREAM_SHOW_LIST

        STREAM_LIST_CLICKS.open().delay(150, TimeUnit.MILLISECONDS) thenDo {
            it.destClazz sendTo STREAM_START_ACTIVITY
        } can this

    }

}

interface MainModelContract {
    fun accessData(): Observable<Iterable<MainModel.Item>>
}