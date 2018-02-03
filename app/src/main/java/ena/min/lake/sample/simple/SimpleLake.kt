package ena.min.lake.sample.simple

import ena.min.android.lake.Cloud
import ena.min.android.lake.EasyLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/2/18.
 */

class SimpleLake : EasyLake() {

    private var isConnected = false
    private val cloud = Cloud()

    init {
        defineStream(STREAM_FINISH, Stream<Unit>(cloud, STREAM_FINISH))
        defineStream(STREAM_UPDATE_TIMER_TEXT, Stream<String>(cloud, STREAM_UPDATE_TIMER_TEXT))
    }

    override fun connect(): SimpleLake {
        super.connect()

        if (isConnected) {
            return this
        }

        isConnected = true

        var time = 10
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe {
                    onClockTick(time--)
                } can this

        return this
    }

    private fun onClockTick(time: Int) {
        "$time" sendTo get(STREAM_UPDATE_TIMER_TEXT)

        if (time == 0) {
            isConnected = false
            Unit sendTo get(STREAM_FINISH)
        }
    }

    companion object {
        val STREAM_UPDATE_TIMER_TEXT = "STREAM_UPDATE_TIMER_TEXT"
        val STREAM_FINISH = "STREAM_FINISH"

        var instance = SimpleLake()
            private set
    }
}
