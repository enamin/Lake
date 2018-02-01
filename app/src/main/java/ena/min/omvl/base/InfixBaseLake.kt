package ena.min.omvl.base

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.*


/**
 * Created by aminenami on 1/27/18.
 */

open class InfixBaseLake : BaseLake(), LakeInfix {
    override val disposables = ArrayList<Disposable?>()

    override fun flush() {
        disposeAll()
        super.flush()
    }
}

open class BaseLake {

    private val afterFlushed = PublishSubject.create<Unit>()

    open fun getViewContract(): BaseViewContract? = null

    fun afterFlushed(): Observable<Unit> = afterFlushed

    open fun flush() {
        getViewContract()?.destroyView()
        afterFlushed.onNext(Unit)
        afterFlushed.onComplete()
    }
}