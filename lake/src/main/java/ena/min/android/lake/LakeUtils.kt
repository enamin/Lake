package ena.min.android.lake

import android.os.Handler
import android.os.Looper
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.switchOnNext
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject

/**
 * Created by aminenami on 2/1/18.
 */


open class EasyLake : Lake(), DisposableCan, AllInfixes {
    override val disposables = ArrayList<Disposable?>()
    override fun disconnect() {
        super.disconnect()
        clearCan()
    }
}

interface DisposableCan {
    val disposables: ArrayList<Disposable?>

    fun clearCan() {
        disposables.forEach { it?.dispose() }
        disposables.clear()
    }

    fun clearCan(disposable: Disposable?) {
        disposables.remove(disposable)
        disposable?.dispose()
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

val appUiThread = Schedulers.from { Handler(Looper.getMainLooper()).post { it.run() } }

interface CloudInfix {

    infix fun <T> Observable<T>.thenDo(func: (T) -> Unit): Disposable? {
        return this.subscribe { func(it as T) }
    }

    infix fun <T : Any> Stream<T>.thenDo(func: ((T) -> Unit)): Disposable {
        return this.open().subscribe { func(it as T) }

    }

//    infix fun <T : Any> Stream<T>.peek(func: (T) -> Unit): Disposable {
//        return this.open().subscribe { func(it as T) }
//    }

    infix fun <T : Any> T.sendTo(stream: Stream<T>) {
        return stream.send(this)
    }

    infix fun <T> Observable<T>.pipeTo(that: Subject<T>): Disposable? {
        return this.subscribe { that.onNext(it) }
    }

    infix fun <T : Any> Observable<T>.pipeTo(that: Stream<T>): Disposable? {
        return this.subscribe {
            Log.d("LAKE_TRANSACTIONS", "pipeTo(): ITEM: $it -> STEAM: $that")
            that.send(it)
        }
    }

//    infix fun <T, K> Observable<T>.pipeTo(that: (T) -> Observable<K>): Observable<K> {
//        return Observable.concat(this.map { that(it) })
//    }

    infix fun <T : Any, K : Any> Stream<T>.pipeTo(that: (T) -> Observable<K>): Observable<K> {
        return this.open().map { that(it) }.switchOnNext()
    }

//    fun <T> Observable<T>.skipCurrent(): Observable<T> {
//        return this.skip(1)
//    }

}
