package ena.min.omvl.base

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * Created by aminenami on 2/1/18.
 */



interface DisposableCan {
    val disposables: ArrayList<Disposable?>
    fun disposeAll() {
        disposables.forEach { it?.dispose() }
    }
    fun can(disposable: Disposable?) {
        disposables.add(disposable)
    }

}

interface LakeInfix : DisposableCan, OceanInfix {

    infix fun Disposable?.can(that: DisposableCan) {
        can(this)
    }
}

interface OceanInfix {
    infix fun <T: Any?> Observable<T>?.act(that: (Any?) -> Unit): Disposable? {
        return this?.subscribe(that)
    }
}

class NO_MODEL : BaseModelContract
class NO_VIEW : BaseViewContract
