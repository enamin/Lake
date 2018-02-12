package ena.min.android.lake

import io.reactivex.subjects.PublishSubject

class Cloud {

    private val subjects = HashMap<String, PublishSubject<in Any>>()

    operator fun get(streamName: String): PublishSubject<in Any> {
        subjects.getOrPut(streamName) {
            val s = PublishSubject.create<Any>()
            subjects[streamName] = s
            s
        }

        return subjects[streamName]!!
    }

    fun has(streamName: String): Boolean {
        return subjects.containsKey(streamName)
    }

    fun send(streamName: String, item: Any = Unit) {
        get(streamName).onNext(item)
    }

    fun sendToAll(item: Any = Unit) {
        subjects.values.forEach { it.onNext(item) }
    }

}

data class Stream<T : Any>(val cloud: Cloud, val streamName: String) {
    var memory: T? = null
        private set

    fun send(item: T) {
        cloud.send(streamName, item)
        memory = item
    }

    fun open(): PublishSubject<T> {
        return cloud[streamName] as PublishSubject<T>
    }
}

interface CloudOwner {
    val cloud: Cloud
    fun send(streamName: String, item: Any = Unit) {
        this.cloud.send(streamName, item)
    }
}