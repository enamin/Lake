package ena.min.lake.sample.list

import ena.min.android.lake.AllInfixes
import ena.min.lake.sample.NetRequestContract
import ena.min.lake.sample.appNetworkLake
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.json.JSONObject

/**
 * Created by aminenami on 2/9/18.
 */

class ListModel : ListModelContract, AllInfixes {
    override fun accessData(): Observable<ListResponse> {

        val responseSubject = PublishSubject.create<ListResponse>()

        appNetworkLake.STREAM_RESPONSE.open().filter { it.name == "list" } thenDo {
            if (it.isFailed) {
                responseSubject.onNext(ListResponse(error = true))
            } else {
                val data = it.json.getJSONArray("data")
                val itemList = List(data.length()) {
                    val json = data.getJSONObject(it)
                    ListResponse.Item(json.getString("title"), json.getString("body"))
                }

                responseSubject.onNext(ListResponse(itemList, false))
            }
        }

        appNetworkLake.STREAM_REQUEST.send(object : NetRequestContract {
            override val name: String = "list"
            override val url: String = "https://jsonplaceholder.typicode.com/posts"
            override val method: String = "GET"
            override val body: JSONObject = JSONObject()
        })

        return responseSubject
    }
}