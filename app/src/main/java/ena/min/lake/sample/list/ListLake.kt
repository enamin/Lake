package ena.min.lake.sample.list

import ena.min.android.lake.CloudLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/9/18.
 */

class ListLake(private val model: ListModelContract) : CloudLake() {
    val STREAM_SHOW_ITEM = streamOf<ListViewModel>()
    val STREAM_ERROR = streamOf<String>()
    val STREAM_CLICKS = streamOf<Unit>()
    val STREAM_UPDATE_UI = streamOf<Iterable<ListUiElements>>()

    private var isBusy = false
    private var requestDisposable: Disposable? = null

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


    private fun requestData() {

        clearCan(requestDisposable)

        requestDisposable = model.accessData() thenDo {
            isBusy = false
            if (it.error) {
                "Connection Problem!" sendTo STREAM_ERROR
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        updateUiElements(listOf(ListUiElements.BUTTON))
                    }
                }, 800)
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


