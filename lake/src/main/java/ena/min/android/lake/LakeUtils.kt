package ena.min.android.lake

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.switchOnNext
import io.reactivex.subjects.Subject
import java.util.*
import kotlin.collections.ArrayList

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

interface InfixDisposableCan: DisposableCan, CloudInfix {
    infix fun <T> Observable<T>?.thenDoSafe(func: (T) -> Unit): Disposable? {
        val d = this thenDo func
        can(d)
        return d
    }

    infix fun <T : Any> Stream<T>?.thenDoSafe(func: ((T) -> Unit)): Disposable? {
        val d = this thenDo func
        can(d)
        return d
    }

    fun <T> Observable<T>?.thenSafe(func: (T) -> Unit): Disposable? {
        val d = this.then(func)
        can(d)
        return d
    }

    infix fun <T> Observable<T>.pipeToSafe(that: Subject<T>): Disposable? {
        val d = this pipeTo that
        can(d)
        return d
    }

    infix fun <T : Any> Observable<T>.pipeToSafe(that: Stream<T>): Disposable? {
        val d = this pipeTo that
        can(d)
        return d
    }

    infix fun <T : Any> Stream<T>.pipeToSafe(that: Stream<T>): Disposable? {
        val d = this pipeTo that
        can(d)
        return d
    }
}

interface AllInfixes : CloudInfix, InfixDisposableCan {

//    infix fun Disposable?.can(that: DisposableCan) {
//        that.can(this)
//    }

}

interface CloudInfix {

    infix fun <T> Observable<T>?.thenDo(func: (T) -> Unit): Disposable? {
        return this?.subscribe { func(it as T) }
    }

    infix fun <T : Any> Stream<T>?.thenDo(func: ((T) -> Unit)): Disposable? {
        return this?.open()?.subscribe { func(it as T) }
    }

    fun <T> Observable<T>?.then(func: (T) -> Unit): Disposable? {
        return this?.subscribe { func(it as T) }
    }

    fun <T> Observable<T>.onEach(func: (T) -> Unit): Observable<T> {
        return this.map { func(it); it }
    }

    fun <T> Observable<T>.schedule(delay: Long, func: (T) -> Unit): Observable<T> {
        return this.map {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    func(it)
                }
            }, delay)
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

    infix fun <T : Any> Stream<T>.pipeTo(that: Stream<T>): Disposable? {
        return this.open().subscribe {
            that.send(it)
        }
    }

    infix fun <T : Any, K : Any> Stream<T>.feedTo(that: (T) -> Observable<K>): Observable<K> {
        return this.open().map { that(it) }.switchOnNext()
    }

}
