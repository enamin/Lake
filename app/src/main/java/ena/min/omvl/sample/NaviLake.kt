package ena.min.omvl.sample

import android.util.Log
import android.widget.FrameLayout
import ena.min.omvl.base.BaseLakeCreator
import ena.min.omvl.base.InfixBaseLake
import ena.min.omvl.base.NO_MODEL
import ena.min.omvl.base.NO_VIEW
import java.util.*

/**
 * Created by aminenami on 1/31/18.
 */


class NaviLake : InfixBaseLake(), BaseLakeCreator<NO_MODEL, NO_VIEW> {
    private val ocean = appOcean
    private var activeWindow: FrameLayout? = null
    private val lakeStack = Stack<InfixBaseLake>()

    override fun create(model: NO_MODEL?, view: NO_VIEW?): InfixBaseLake {

        ocean[STREAM.naviActiveWindow].filter { it !== activeWindow } act {
            this.activeWindow?.removeAllViews()
            this.activeWindow = it as? FrameLayout
        } can this

        ocean[STREAM.naviPop] act {
            try {
                lakeStack.pop().flush()
            } catch (e: EmptyStackException) {
            }
        } can this

        ocean[STREAM.naviPushLake].filter { it is InfixBaseLake } act {
            val lake = (it as? InfixBaseLake) ?: return@act
            val vc = lake.getViewContract() ?: return@act
            val ctx = activeWindow?.context ?: return@act
            activeWindow?.addView(vc.createView(ctx))
            lakeStack.push(lake)
            Log.d("aminenami", "stack: ${lakeStack.size}, $lakeStack")
            ocean.emit(STREAM.naviLakeStack, lakeStack)
        } can this

        return this
    }
}