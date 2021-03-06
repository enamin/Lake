package ena.min.lake.sample.simple

import ena.min.android.lake.CloudLake
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/2/18.
 */

class SimpleLake private constructor() : CloudLake() {

    val STREAM_FINISH = streamOf<Unit>()
    val STREAM_UPDATE_TIMER_TEXT = streamOf<String>()

    override fun connect() {
        super.connect()

        var time = 10
        Observable.interval(1, TimeUnit.SECONDS) thenDo {
            onClockTick(time--)
        }
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
