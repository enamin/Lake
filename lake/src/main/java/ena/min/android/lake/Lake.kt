package ena.min.android.lake

/**
 * Created by aminenami on 2/1/18.
 */


open class Lake {

    open val streams = HashMap<String, Stream<*>>()

    fun defineStream(key: String, stream: Stream<*>) {
        streams[key] = stream
    }

    operator fun get(streamKey: String): Stream<*>? = streams[streamKey]

    open fun connect(): Lake = also {

    }

}
