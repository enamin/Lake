package ena.min.android.lake

import android.os.Handler
import android.os.Looper
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

/**
 * Created by aminenami on 2/1/18.
 */


open class EasyLake : Lake(), DisposableCan, AllInfixes {
    override val disposables = ArrayList<Disposable?>()
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

interface AllInfixes : CloudInfix {

    infix fun Disposable?.can(that: DisposableCan) {
        that.can(this)
    }

}

class SendHelper(val cloud: Cloud?, val item: Any?) {
    fun sendToAll() = cloud?.sendToAll(item)
    fun send(streamName: String) = cloud?.send(streamName, item)
}

val appUiThread = Schedulers.from { Handler(Looper.getMainLooper()).post { it.run() } }

interface CloudInfix {

//    infix fun <T : Any?> Observable<T>?.perform(that: (T?) -> Unit): Disposable? {
//        return this?.subscribeOn(Schedulers.newThread())?.subscribe(that)
//    }

    infix fun <T> Observable<T>?.perform(func: (T?)-> Unit): Disposable? {
        return this?.subscribe(func)
    }

    infix fun <T: Any?> Stream<T>?.perform(func: (T?)->Unit): Disposable? {
        return this?.cloud?.get(this.streamName)?.subscribe(func as (Any?)->Unit)
    }

    infix fun String.from(po: CloudOwner): BehaviorSubject<Any?> {
        return po.cloud[this]
    }

    infix fun String.from(o: Cloud): BehaviorSubject<Any?> {
        return o[this]
    }

//    infix fun Any?.sendTo(c: Cloud?): SendHelper {
//        return SendHelper(c, this)
//    }
//
//    infix fun Any?.sendTo(co: CloudOwner?): SendHelper {
//        return SendHelper(co?.cloud, this)
//    }

    infix fun Any?.sendTo(stream: Stream<*>?): Unit? {
        return stream?.cloud?.send(stream.streamName, this)
    }

    infix fun SendHelper.via(streamName: String): Unit? {
        return this.cloud?.send(streamName, this.item)
    }

    infix fun SendHelper.via(streamNames: Iterable<String>): Unit? {
        return streamNames.forEach { this.cloud?.send(it, this.item) }
    }

    infix fun <T> Observable<T>?.pipeTo(that: Subject<T>): Disposable? {
        return this?.subscribe { that.onNext(it) }
    }

    infix fun <T> Observable<T>?.pipeTo(that: Stream<T>): Disposable? {
        return this?.subscribe { that.send(it)}
    }

}
