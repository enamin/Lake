package ena.min.android.lake

import io.reactivex.subjects.BehaviorSubject

class Cloud {

    private val bank = HashMap<String, BehaviorSubject<in Any?>>()

    fun has(streamName: String): Boolean {
        return bank.containsKey(streamName)
    }

    fun remove(streamName: String) {
        get(streamName).onComplete()
        bank.remove(streamName)
    }

    operator fun get(streamName: String): BehaviorSubject<in Any?> {
        bank.getOrPut(streamName) {
            val s = BehaviorSubject.create<Any?>()
            bank[streamName] = s
            s
        }

        return bank[streamName]!!
    }

    fun send(streamName: String, item: Any? = Unit) {
        get(streamName).onNext(item)
    }

    fun sendToAll(item: Any? = Unit) {
        bank.values.forEach { it.onNext(item) }
    }

}

data class Stream<T: Any?>(val cloud: Cloud, val streamName: String) {
    fun send(item: T) {
        cloud.send(streamName, item)
    }
    fun open() = cloud[streamName]
}

interface CloudOwner {
    val cloud: Cloud
    fun send(streamName: String, item: Any? = Unit) {
        this.cloud.send(streamName, item)
    }
}