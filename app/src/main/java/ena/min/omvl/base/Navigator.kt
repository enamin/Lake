package ena.min.omvl.base

import android.widget.FrameLayout
import io.reactivex.Observable

/**
 * Created by aminenami on 1/28/18.
 */

open class Navigator<T>(val navigatorHandler: NavigatorHandler<T>) {


    class NullLake : InfixBaseLake() {
        override fun getViewContract(): BaseViewContract? {
            return null
        }
    }

    interface NavigatorHandler<T> {
        fun navigationStream(): Observable<T>
        fun navigationEmit(command: T)
        fun getActiveWindow(): FrameLayout?
    }


}