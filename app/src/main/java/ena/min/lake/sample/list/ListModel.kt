package ena.min.lake.sample.list

import ena.min.android.lake.AllInfixes
import ena.min.lake.sample.NetRequestContract
import ena.min.lake.sample.appNetworkLake
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by aminenami on 2/9/18.
 */

class ListModel : ListModelContract, AllInfixes {
    override fun accessData(): Observable<ListResponse> {
        appNetworkLake.STREAM_REQUEST.send(object : NetRequestContract {
            override val name: String = "list"
            override val url: String = "https://jsonplaceholder.typicode.com/posts"
            override val method: String = "GET"
            override val body: JSONObject = JSONObject()
        })


        return appNetworkLake.STREAM_RESPONSE.open().filter { it.name == "list" }.map {
            if (it.isFailed) {
                ListResponse(error = true)
            } else {
                val data = JSONArray(it.response)
                val itemList = List(data.length()) {
                    val json = data.getJSONObject(it)
                    ListResponse.Item(json.getString("title"), json.getString("body"))
                }

                ListResponse(itemList, false)
            }
        }

//        val responseSubject = PublishSubject.create<ListResponse>()
//
//        appNetworkLake.STREAM_RESPONSE.open().filter { it.name == "list" } thenDo {
//            if (it.isFailed) {
//                responseSubject.onNext(ListResponse(error = true))
//            } else {
//                val data = JSONArray(it.response)
//                val itemList = List(data.length()) {
//                    val json = data.getJSONObject(it)
//                    ListResponse.Item(json.getString("title"), json.getString("body"))
//                }
//
//                responseSubject.onNext(ListResponse(itemList, false))
//            }
//
//            responseSubject.onComplete()
//        }

//        return responseSubject
    }
}