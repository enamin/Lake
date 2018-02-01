package ena.min.omvl.util

import ena.min.omvl.base.InfixBaseLake
import ena.min.omvl.sample.STREAM
import ena.min.omvl.sample.appOcean
import io.reactivex.Observable
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */

fun watchTopOnBack(): Observable<Any?>? {
    return appOcean.getWithLatestFrom(STREAM.naviBack, STREAM.naviLakeStack)
    { _, stack ->
        try {
            (stack as? Stack<*>)?.peek()
        } catch (e: EmptyStackException) {
            null
        }
    }
}

fun watchTapOnFor(target: InfixBaseLake): Observable<Any?>? {
    return watchTopOnBack()?.filter { it === target }
}



