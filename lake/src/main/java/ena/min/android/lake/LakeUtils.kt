package ena.min.android.lake

import android.os.Handler
import android.os.Looper
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.switchOnNext
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */

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

    fun <T> Observable<T>.then(func: (T) -> Unit): Disposable? {
        return this.subscribe { func(it as T) }
    }

    fun <T> Observable<T>.onEach(func: (T) -> Unit): Observable<T> {
        return this.map { func(it); it }
    }

    fun <T> Observable<T>.schedule(delay: Long, func: (T) -> Unit): Observable<T> {
        return this.map {
            Timer().schedule(object : TimerTask(){
                override fun run() {
                    func(it)
                }
            },delay)
            it
        }
    }

    infix fun <T : Any> T.sendTo(stream: Stream<T>) {
        return stream.send(this)
    }

    infix fun <T> Observable<T>.pipeTo(that: Subject<T>): Disposable? {
        return this.subscribe { that.onNext(it) }
    }

    infix fun <T : Any> Observable<T>.pipeTo(that: Stream<T>): Disposable? {
        return this.subscribe {
            that.send(it)
        }
    }

    infix fun <T : Any, K : Any> Stream<T>.pipeTo(that: (T) -> Observable<K>): Observable<K> {
        return this.open().map { that(it) }.switchOnNext()
    }

}
