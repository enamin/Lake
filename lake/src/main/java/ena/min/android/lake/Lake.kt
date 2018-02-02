package ena.min.lake

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by aminenami on 2/1/18.
 */


open class Lake<M : ModelContract, V : ViewContract> {
    var model: M? = null
        internal set
    var view: V? = null
        internal set

    private val afterFlushed = PublishSubject.create<Unit>()

    fun afterFlushed(): Observable<Unit> = afterFlushed

    open fun flush() {
        view?.destroyView()
        afterFlushed.onNext(Unit)
        afterFlushed.onComplete()
    }

    open fun connect(model: M? = null, view: V? = null): Lake<M, V> = also {
        this.model = model
        this.view = view
    }

}

open class LakeView(val viewResId: Int) : ViewContract {
    var view: View? = null
        private set

    private fun detachView(): View? {
        (view?.parent as? ViewGroup)?.removeView(view)
        return view
    }

    override fun createView(context: Context): View? {
        view = LayoutInflater.from(context).inflate(viewResId, null, false)
        return view
    }

    override fun destroyView() {
        detachView()
        view = null
    }
}

class NO_MODEL : ModelContract
class NO_VIEW : ViewContract

open class LakeModel : ModelContract

interface ViewContract {
    fun createView(context: Context): View? = null
    fun destroyView() = Unit
}

interface ModelContract