package ena.min.lake.sample.simple

import ena.min.android.lake.Cloud
import ena.min.android.lake.EasyLake
import ena.min.android.lake.Stream
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/2/18.
 */

class SimpleLake private constructor() : EasyLake() {
    private val cloud = Cloud()

    val STREAM_FINISH = Stream<Unit>(cloud, "STREAM_FINISH")
    val STREAM_UPDATE_TIMER_TEXT = Stream<String>(cloud, "STREAM_UPDATE_TIMER_TEXT")

    override fun connect() {
        super.connect()

        var time = 10
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe {
                    onClockTick(time--)
                } can this
    }

    private fun onClockTick(time: Int) {
        "$time" sendTo STREAM_UPDATE_TIMER_TEXT

        if (time == 0) {
            Unit sendTo STREAM_FINISH
            disconnect()
        }
    }

    companion object {
        var instance = SimpleLake()
            private set
    }
}
