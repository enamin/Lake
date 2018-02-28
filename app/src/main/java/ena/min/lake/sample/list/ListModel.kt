package ena.min.lake.sample.list

import ena.min.android.lake.AllInfixes
import ena.min.android.lake.CloudInfix
import ena.min.lake.sample.NetRequestContract
import ena.min.lake.sample.NetResponseContract
import ena.min.lake.sample.appNetworkLake
import io.reactivex.Observable
import org.json.JSONArray

/**
 * Created by aminenami on 2/9/18.
 */

class ListModel : ListModelContract, CloudInfix {

    override fun accessData(): Observable<ListResponse> {

        val request = object : NetRequestContract {
            override val name: String = "list"
            override val url: String = "https://jsonplaceholder.typicode.com/posts"
        }

        request sendTo appNetworkLake.STREAM_REQUEST

        return appNetworkLake.STREAM_RESPONSE.open()
                .filter { it.name == "list" }
                .map { createResponse(it) }
    }

    private fun createResponse(respContract: NetResponseContract): ListResponse {
        return if (respContract.isFailed) {
            ListResponse(error = true)
        } else {
            val data = JSONArray(respContract.response)
            val itemList = List(data.length()) {
                val json = data.getJSONObject(it)
                ListResponse.Item(json.getString("title"), json.getString("body"))
            }

            ListResponse(itemList, false)
        }
    }
}