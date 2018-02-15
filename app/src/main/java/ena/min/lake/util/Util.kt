package ena.min.lake.util

import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */

fun doAfter(delay: Long, job: ()->Unit) {
    Timer().schedule(object: TimerTask(){
        override fun run() {
            job()
        }
    }, delay)
}