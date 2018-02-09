package ena.min.lake.sample

import ena.min.android.lake.Cloud
import ena.min.android.lake.EasyLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import org.json.JSONObject

/**
 * This lake handles all of our networking needs. It doesn't have any dependencies on
 * App Network Layer! the only thing it cares about is NetworkLayerContract. Check App.kt, we create,
 * connect and store only one instance from this lake. Therefore, all different parts of our program
 * can access and share all the different responses which this lake streams.
 */

class NetworkLake(val networkLayerContract: NetworkLayerContract) : EasyLake() {
    val cloud = Cloud()
    val STREAM_REQUEST = Stream<NetRequestContract>(cloud, "STREAM_REQUEST")
    val STREAM_RESPONSE = Stream<NetResponseContract>(cloud, "STREAM_RESPONSE")

    override fun connect() {
        super.connect()

        val responder = networkLayerContract::respond
        STREAM_REQUEST pipeTo responder pipeTo STREAM_RESPONSE
    }
}

interface NetRequestContract {
    val name: String
    val url: String
    val method: String
    val body: JSONObject
}

interface NetResponseContract {
    val name: String
    val netRequest: NetRequestContract
    val json: JSONObject
    val isFailed: Boolean
    val code: Int
}

interface NetworkLayerContract {
    fun respond(netRequestContract: NetRequestContract): Observable<NetResponseContract>
}
