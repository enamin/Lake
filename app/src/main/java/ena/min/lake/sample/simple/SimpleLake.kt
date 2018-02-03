package ena.min.lake.sample.simple

import android.util.Log
import ena.min.lake.EasyLake
import ena.min.lake.NO_MODEL
import ena.min.lake.ViewContract
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/2/18.
 */

class SimpleLake : EasyLake<NO_MODEL, SimpleViewContract>() {
    private var isConnected = false

    override fun connect(model: NO_MODEL?, view: SimpleViewContract?): SimpleLake {
        super.connect(model, view)

        Log.e("SimpleLake", "view is: $view")

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
        view?.updateTimerText("$time")
        if (time == 0) {
            isConnected = false
            flush()
        }
    }

    companion object {
        var instance = SimpleLake()
            private set
    }
}

interface SimpleViewContract : ViewContract {
    fun updateTimerText(text: String)
}