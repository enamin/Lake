package ena.min.lake.sample.list

import android.util.Log
import ena.min.android.lake.Cloud
import ena.min.android.lake.EasyLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/9/18.
 */

class ListLake(private val model: ListModelContract) : EasyLake() {
    private val cloud = Cloud()
    val STREAM_SHOW_ITEM = Stream<ListViewModel>(cloud, "STREAM_SHOW_ITEM")
    val STREAM_ERROR = Stream<String>(cloud, "STREAM_ERROR")
    val STREAM_CLICKS = Stream<Unit>(cloud, "STREAM_CLICKS")
    val STREAM_UPDATE_UI = Stream<Iterable<ListUiElements>>(cloud, "STREAM_UPDATE_UI")

    private var isBusy = false

    override fun connect() {
        super.connect()

        STREAM_CLICKS.open().filter { !isBusy } thenDo {
            isBusy = true
            requestData()
            updateUiElements(listOf(ListUiElements.LOADING))
        } can this

    }

    private fun updateUiElements(elements: Iterable<ListUiElements>) {
        elements sendTo STREAM_UPDATE_UI
    }

    private var requestDisposable: Disposable? = null

    private fun requestData() {
        requestDisposable?.dispose()
        disposables.remove(requestDisposable)
        requestDisposable = model.accessData() thenDo {
            isBusy = false
            if (it.error) {
                updateUiElements(listOf(ListUiElements.BUTTON))
                "Connection Problem!" sendTo STREAM_ERROR
            } else {
                updateUiElements(listOf(ListUiElements.LIST))
                sendIntervalOutput(it.list)
            }
        }
        requestDisposable can this
    }

    private fun sendIntervalOutput(list: Iterable<ListResponse.Item>) {
        Observable.interval(300, TimeUnit.MILLISECONDS)
                .zipWith(list) { _, item ->
                    ListViewModel(item.title, item.body)
                } pipeTo STREAM_SHOW_ITEM
    }
}

enum class ListUiElements {
    BUTTON, LOADING, LIST
}

open class ListViewModel(var title: String = "", var body: String = "")

interface ListModelContract {
    fun accessData(): Observable<ListResponse>
}

data class ListResponse(val list: Iterable<Item> = emptyList(), val error: Boolean = false) {
    class Item(val title: String = "", val body: String = "")
}


