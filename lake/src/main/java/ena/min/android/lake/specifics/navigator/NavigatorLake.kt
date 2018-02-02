package ena.min.android.lake.specifics.navigator

import android.util.Log
import android.view.ViewGroup
import ena.min.lake.InfixLake
import ena.min.lake.NO_MODEL
import ena.min.lake.Ocean
import ena.min.lake.OceanOwner
import io.reactivex.Observable
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */

class NavigatorLake : InfixLake<NO_MODEL, NavigatorViewContract>(), OceanOwner {
    override val ocean = Ocean()
    private val lakeStack = Stack<InfixLake<*, *>>()

    object Streams {
        val back = "back"
        val push = "push"
        val pop = "pop"
        val activeWindow = "activeWindow"
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
            val lake = (it as? InfixLake<*, *>) ?: return@perform
            val vc = it.view?: return@perform

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

    fun watchTopOnBackFor(target: InfixLake<*, *>): Observable<Any?>? {
        return watchTopOnBack()?.filter { it === target }
    }

    override fun flush() {
        lakeStack.empty()
        super.flush()
    }

}