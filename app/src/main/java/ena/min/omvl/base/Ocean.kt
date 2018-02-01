package ena.min.omvl.base

import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.PublishSubject

        /**
         * Created by aminenami on 1/27/18.
         */

typealias Transform2to1 = (Any?, Any?) -> Any?

class Ocean {

    private val bank = HashMap<String, PublishSubject<in Any?>>()

    fun has(streamName: String): Boolean {
        return bank.containsKey(streamName)
    }

    fun remove(streamName: String) {
        bank[streamName]?.onComplete()
        bank.remove(streamName)
    }

    fun getWithLatestFrom(streamNameSource: String, streamTarget: String, transform: Transform2to1): Observable<Any?>? {
        return get(streamNameSource)
                .withLatestFrom(get(streamTarget))
                { s1: Any?, s2: Any? -> transform(s1, s2) }
    }

    operator fun get(streamName: String): PublishSubject<Any?> {
        bank.getOrPut(streamName) {
            val ps = PublishSubject.create<Any?>()
            bank[streamName] = ps
            ps
        }

        return bank[streamName]!!
    }

    fun emit(streamName: String, item: Any?) {
        bank[streamName]?.onNext(item)
    }

}