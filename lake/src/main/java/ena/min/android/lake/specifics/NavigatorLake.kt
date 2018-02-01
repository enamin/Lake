package ena.min.android.lake.specifics

import android.util.Log
import android.view.ViewGroup
import ena.min.lake.*
import io.reactivex.Observable
import io.reactivex.rxkotlin.withLatestFrom
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */

class NavigatorLake : InfixLake<NO_MODEL, NO_VIEW>(), OceanOwner {
    override val ocean = Ocean()
    private val lakeStack = Stack<InfixLake<*, *>>()

    object Streams {
        val back = "back"
        val push = "push"
        val pop = "pop"
        val activeWindow = "activeWindow"
        val stackChanged = "stackChanged"
    }

    init {
        connect(NO_MODEL(), NO_VIEW())
    }

    override fun connect(model: NO_MODEL, view: NO_VIEW): NavigatorLake {
        super.connect(model, view)


//        (Streams.activeWindow from this).filter { it !== activeWindow } perform {
//            this.activeWindow?.removeAllViews()
//            this.activeWindow = it as? ViewGroup
//        } can this

        (Streams.pop from this) perform {
            try {
                lakeStack.pop().flush()
            } catch (e: EmptyStackException) {
            }
        } can this

        (Streams.push from this).withLatestFrom(Streams.activeWindow from this) { p, aw -> p to aw }
                .filter { it.first is InfixLake<*, *> } perform {
            val lake = (it?.first as? InfixLake<*, *>) ?: return@perform
            val ctx = (it.second as? ViewGroup)?.context ?: return@perform
            val vc = lake.view
            (it.second as? ViewGroup)?.addView(vc.createView(ctx))
            lakeStack.push(lake)
            Log.d("aminenami", "stack: ${lakeStack.size}, $lakeStack")
            send(Streams.stackChanged, lakeStack)
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