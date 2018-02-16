package ena.min.lake.util

import android.os.Handler
import android.os.Looper
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */

val appUiThread = Schedulers.from { Handler(Looper.getMainLooper()).post { it.run() } }

fun doAfter(delay: Long, job: ()->Unit) {
    Timer().schedule(object: TimerTask(){
        override fun run() {
            job()
        }
    }, delay)
}