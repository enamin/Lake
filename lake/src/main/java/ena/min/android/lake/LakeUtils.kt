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

interface Bin {
    val disposables: ArrayList<Disposable?>

    fun clearBin() {
        disposables.forEach { it?.dispose() }
        disposables.clear()
    }

    fun clearBin(disposable: Disposable?) {
        disposables.remove(disposable)
        disposable?.dispose()
    }

    fun bin(disposable: Disposable?) {
        disposables.add(disposable)
    }

}

interface BinInfix : Bin, CloudInfix {
    infix fun <T> Observable<T>?.thenDo(func: (T) -> Unit): Disposable? {
        val d = this unsafeThenDo func
        bin(d)
        return d
    }

    infix fun <T : Any> Stream<T>?.thenDo(func: ((T) -> Unit)): Disposable? {
        val d = this unsafeThenDo func
        bin(d)
        return d
    }

    fun <T> Observable<T>?.then(func: (T) -> Unit): Disposable? {
        val d = this.unsafeThen(func)
        bin(d)
        return d
    }

    infix fun <T> Observable<T>.pipeTo(that: Subject<T>): Disposable? {
        val d = this unsafePipeTo that
        bin(d)
        return d
    }

    infix fun <T : Any> Observable<T>.pipeTo(that: Stream<T>): Disposable? {
        val d = this unsafePipeTo that
        bin(d)
        return d
    }

    infix fun <T : Any> Stream<T>.pipeTo(that: Stream<T>): Disposable? {
        val d = this unsafePipeTo that
        bin(d)
        return d
    }
}

interface CloudInfix {

    infix fun <T> Observable<T>?.unsafeThenDo(func: (T) -> Unit): Disposable? {
        return this?.subscribe { func(it as T) }
    }

    infix fun <T : Any> Stream<T>?.unsafeThenDo(func: ((T) -> Unit)): Disposable? {
        return this?.open()?.subscribe { func(it as T) }
    }

    fun <T> Observable<T>?.unsafeThen(func: (T) -> Unit): Disposable? {
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

    infix fun <T> Observable<T>.unsafePipeTo(that: Subject<T>): Disposable? {
        return this.subscribe { that.onNext(it) }
    }

    infix fun <T : Any> Observable<T>.unsafePipeTo(that: Stream<T>): Disposable? {
        return this.subscribe {
            that.send(it)
        }
    }

    infix fun <T : Any> Stream<T>.unsafePipeTo(that: Stream<T>): Disposable? {
        return this.open().subscribe {
            that.send(it)
        }
    }

    infix fun <T : Any, K : Any> Stream<T>.feedTo(that: (T) -> Observable<K>): Observable<K> {
        return this.open().map { that(it) }.switchOnNext()
    }

}

interface AllInfixes : BinInfix {

}