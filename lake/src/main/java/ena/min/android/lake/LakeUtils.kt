package ena.min.lake

import android.os.Handler
import android.os.Looper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */


open class EasyLake<M : ModelContract, V : ViewContract> : Lake<M, V>(), DisposableCan, AllInfixes {
    override val disposables = ArrayList<Disposable?>()

    override fun flush() {
        disposeAll()
        super.flush()
    }
}

interface DisposableCan {
    val disposables: ArrayList<Disposable?>
    fun disposeAll() {
        disposables.forEach { it?.dispose() }
    }

    fun can(disposable: Disposable?) {
        disposables.add(disposable)
    }

}

interface AllInfixes : OceanInfix {

    infix fun Disposable?.can(that: DisposableCan) {
        that.can(this)
    }

}

class SendHelper(val ocean: Ocean, val item: Any?) {
    fun sendToAll() = ocean.sendToAll(item)
    fun send(streamName: String) = ocean.send(streamName, item)
}

val appUiThread = Schedulers.from { Handler(Looper.getMainLooper()).post { it.run() } }

interface OceanInfix {

    infix fun <T : Any?> Observable<T>?.perform(that: (T?) -> Unit): Disposable? {
        return this?.subscribeOn(Schedulers.newThread())?.observeOn(appUiThread)?.subscribe(that)
    }

    infix fun String.from(po: OceanOwner): PublishSubject<Any?> {
        return po.ocean[this]
    }

    infix fun String.from(o: Ocean): PublishSubject<Any?> {
        return o[this]
    }

    infix fun Any?.sendTo(o: Ocean): SendHelper {
        return SendHelper(o, this)
    }

    infix fun Any?.sendTo(oo: OceanOwner): SendHelper {
        return SendHelper(oo.ocean, this)
    }

    infix fun SendHelper.via(streamName: String) {
        return this.ocean.send(streamName, this.item)
    }

    infix fun SendHelper.via(streamNames: Iterable<String>) {
        return streamNames.forEach { this.ocean.send(it, this.item) }
    }
}
