package ena.min.android.lake

/**
 * Created by aminenami on 2/1/18.
 */


open class Lake {

    open val streams = HashMap<String, Stream<*>>()
    var isConnected: Boolean = false
    private set

    fun defineStream(key: String, stream: Stream<*>) {
        streams[key] = stream
    }

    operator fun get(streamKey: String): Stream<*>? = streams[streamKey]

    open fun connect() {
        isConnected = true
    }

    open fun disconnect() {
        isConnected = false
    }

}
