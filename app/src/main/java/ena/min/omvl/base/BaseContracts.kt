package ena.min.omvl.base

import android.content.Context
import android.view.View

/**
 * Created by aminenami on 1/28/18.
 */

interface BaseViewContract {
    fun detachAndReturnView(): View? = null
    fun createView(context: Context): View? = null
    fun destroyView() = Unit
}

interface BaseModelContract


interface BaseLakeCreator<M : BaseModelContract?, V : BaseViewContract?> {
    fun create(model: M? = null, view: V? = null): InfixBaseLake
    fun create() = create(null, null)
}