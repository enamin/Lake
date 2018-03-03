package ena.min.android.lake

import io.reactivex.disposables.Disposable
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */


open class Lake {

    var isConnected: Boolean = false
        private set

    open fun connect() {
        isConnected = true
    }

    open fun disconnect() {
        isConnected = false
    }

}

open class CloudLake : Lake(), CloudOwner, AllInfixes {
    override val cloud = Cloud()
    override val disposables = ArrayList<Disposable?>()

    fun <T : Any> streamOf() = Stream<T>(cloud, UUID.randomUUID().toString())

    override fun disconnect() {
        super.disconnect()
        clearBin()
//        cloud.completeAll()
    }
}