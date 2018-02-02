package ena.min.android.lake.specifics.navigator

import android.util.Log
import ena.min.lake.EasyLake
import ena.min.lake.NO_MODEL
import ena.min.lake.Ocean
import ena.min.lake.OceanOwner
import io.reactivex.Observable
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */

class NavigatorLake : EasyLake<NO_MODEL, NavigatorViewContract>(), OceanOwner {
    override val ocean = Ocean()
    private val lakeStack = Stack<EasyLake<*, *>>()

    object Streams {
        val back = "back"
        val push = "push"
        val pop = "pop"
        val stackChanged = "stackChanged"
    }

    override fun connect(model: NO_MODEL?, view: NavigatorViewContract?): NavigatorLake {
        super.connect(model, view)

        (Streams.pop from this) perform {
            try {
                lakeStack.pop().flush()
            } catch (e: EmptyStackException) {
            }
        } can this

        (Streams.push from this).perform {
            val lake = (it as? EasyLake<*, *>) ?: return@perform
            val vc = it.view ?: return@perform

            if (view?.navigationShowView(vc) == true) {
                lakeStack.push(lake)
                Log.d("aminenami", "stack: ${lakeStack.size}, $lakeStack")
                send(Streams.stackChanged, lakeStack)
            }
        } can this

        return this
    }

    fun watchTopOnBack(): Observable<Any?>? {
        return ocean.getWithLatestFrom(Streams.back, Streams.stackChanged)
        { _, stack ->
            try {
                (stack as? Stack<*>)?.peek()
            } catch (e: EmptyStackException) {
                null
            }
        }
    }

    fun watchTopOnBackFor(target: EasyLake<*, *>): Observable<Any?>? {
        return watchTopOnBack()?.filter { it === target }
    }

    override fun flush() {
        lakeStack.empty()
        super.flush()
    }

}