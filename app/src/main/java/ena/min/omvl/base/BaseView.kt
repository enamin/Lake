package ena.min.omvl.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by aminenami on 1/27/18.
 */

open class BaseView(val viewResId: Int): BaseViewContract {
    var view: View? = null

    override fun detachAndReturnView(): View? {
        (view?.parent as? ViewGroup)?.removeView(view)
        return view
    }

    override fun createView(context: Context): View? {
        view = LayoutInflater.from(context).inflate(viewResId, null, false)
        return view
    }

    override fun destroyView() {
        detachAndReturnView()
        view = null
    }
}