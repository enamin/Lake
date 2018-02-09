package ena.min.lake.sample.list

import ena.min.android.lake.Cloud
import ena.min.android.lake.EasyLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/9/18.
 */

class ListLake(private val model: ListModelContract) : EasyLake() {
    private val cloud = Cloud()
    val STREAM_SHOW_ITEM = Stream<ListViewModel>(cloud, "STREAM_SHOW_ITEM")
    val STREAM_ERROR = Stream<String>(cloud, "STREAM_ERROR")

    override fun connect() {
        super.connect()

        model.accessData() thenDo {
            if (it.error) {
                "Connection Problem!" sendTo STREAM_ERROR
            } else {
                sendTimedOutput(it.list)
            }
        } can this
    }

    private fun sendTimedOutput(list: Iterable<ListResponse.Item>) {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .zipWith(list) { _, item ->
                    ListViewModel(item.title, item.body)
                } pipeTo STREAM_SHOW_ITEM
    }
}

open class ListViewModel(var title: String = "", var body: String = "")

interface ListModelContract {
    fun accessData(): Observable<ListResponse>
}

data class ListResponse(val list: Iterable<Item> = emptyList(), val error: Boolean = false) {
    class Item(val title: String = "", val body: String = "")
}


