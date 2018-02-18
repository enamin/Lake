package ena.min.android.lake

import io.reactivex.subjects.PublishSubject

/**
 * Cloud is nothing but a bank of different PublishSubjects which are signified with a
 * streamName <String>. Any component which has access to the cloud can emit an item or
 * observe on emitted items. You should define a cloud for each scope inside your app
 * and pass its instance to any component which needs to access that scope.
 * A better way is to define a CloudLake for each scope. Because a CloudLake contains a
 * single cloud and can also define the standard streams for the scope and manage the
 * underlying logic between streams of data. So you hardly need to work directly with
 * this class and most of times you just need to work with Stream and CloudLake classes.
 */
class Cloud {

    private val subjects = HashMap<String, PublishSubject<in Any>>()

    /**
     * This method returns the PublishSubject indicated by the provided streamName.
     * if there is no such Subject, it creates one and returns it
     * @param streamName the name assigned to the PublishSubject in this cloud
     * @return The PublishSubject corresponding to the streamName
     */
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


/**
 * Stream class wraps a Cloud with a StreamName. Several streams can share the same Cloud.
 * It's a useful class when you don't want to rewrite the name of your streams or the instance of
 * your clouds over and over again. You can observe on items emitted by a Stream the same way as you
 * observe on a cloud. also you can send (emit) items in the same way too.
 * Furthermore, each stream has a "memory" which is the last emitted item. So you can
 * always access the last item which your clouds emit, if your logic needs them. (In this sense
 * a stream changes the Cloud's PublishSubjects into BehaviorSubjects)
 */
data class Stream<T : Any>(val cloud: Cloud, val streamName: String) {

    /**
     * @return The last emitted item by this stream. Otherwise it's null.
     */
    var memory: T? = null
        private set

    /**
     * Emit an item through this stream. The item will be sent via this stream's cloud using this stream name.
     * @param item The item to be emitted
     */
    fun send(item: T) {
        cloud.send(streamName, item)
        memory = item
    }

    /**
     * This method returns the underlying PublishSubject behind this stream. it's useful when you
     * want to add more modifiers (filters, maps, etc) to the stream before consuming its emitted
     * items.
     * @return The underlying PublishSubject behind this stream.
     */

    fun open(): PublishSubject<T> {
        return cloud[streamName] as PublishSubject<T>
    }

    /**
     * @return true if this stream has emitted some items. The last emitted item is saved inside the memory.
     */
    fun hasMemory() = this.memory != null
}

interface CloudOwner {
    val cloud: Cloud
    fun send(streamName: String, item: Any = Unit) {
        this.cloud.send(streamName, item)
    }
}