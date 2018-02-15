package ena.min.lake.sample

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import okhttp3.*
import java.io.IOException

/**
 * This is a simple OkHttp wrapper which creates a request and send back the response via an Observable
 * It doesn't have any dependencies to any of our Classes, it only works with 2 interfaces which are
 * NetRequestContract & NetResponseContract so if we want to replace OkHttp with Retrofit or
 * completely erase and replace this layer, it has no effect outside!
 */

class NetworkLayer : NetworkLayerContract {
    private val okHttpClient = OkHttpClient.Builder().build()

    override fun respond(netRequestContract: NetRequestContract): Observable<NetResponseContract> {
        val pSubject = PublishSubject.create<NetResponseContract>()

        fun _send(isFailed: Boolean, code: Int = -1000, response: String = "") {
            pSubject.onNext(
                    object : NetResponseContract {
                        override val response = response
                        override val isFailed = isFailed
                        override val name = netRequestContract.name
                        override val netRequest = netRequestContract
                        override val code = code
                    }
            )
            pSubject.onComplete()
        }

        val req = Request.Builder()
                .url(netRequestContract.url)
                .build()

        okHttpClient.newCall(req).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                _send(true)
            }

            override fun onResponse(call: Call?, response: Response?) {
                val str = response?.body()?.string()
                if (str == null) {
                    _send(true)
                } else {
                    _send(false, response.code(), str)
                }
            }

        })

        return pSubject
    }
}