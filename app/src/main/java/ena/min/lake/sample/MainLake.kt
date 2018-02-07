package ena.min.lake.sample

import ena.min.android.lake.Cloud
import ena.min.android.lake.EasyLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/3/18.
 */
class MainLake(val model: MainModelContract) : EasyLake() {
    private val cloud = Cloud()

    val STREAM_START_ACTIVITY = Stream<Class<*>>(cloud, "STREAM_START_ACTIVITY")
    val STREAM_LIST_CLICKS = Stream<MainModel.Item>(cloud, "STREAM_LIST_CLICKS")
    val STREAM_SHOW_LIST = Stream<Iterable<MainModel.Item>>(cloud, "STREAM_SHOW_LIST")


    override fun connect() {
        super.connect()

        model.accessData() pipeTo STREAM_SHOW_LIST

        STREAM_LIST_CLICKS.open().delay(300, TimeUnit.MILLISECONDS) thenDo {
            (it as? MainModel.Item?)?.destClazz sendTo STREAM_START_ACTIVITY
        } can this

    }

}

interface MainModelContract {
    fun accessData(): Observable<Iterable<MainModel.Item>>
}

interface MainViewContract {
    val listClicks: Observable<MainModel.Item>
    fun showList(items: Iterable<MainModel.Item>)
    fun startActivity(clazz: Class<*>)
}