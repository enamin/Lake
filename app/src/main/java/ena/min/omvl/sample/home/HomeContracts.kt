package ena.min.omvl.sample.home

import ena.min.omvl.base.BaseModelContract
import ena.min.omvl.base.BaseViewContract
import io.reactivex.Observable

/**
 * Created by aminenami on 1/28/18.
 */

interface HomeViewContract: BaseViewContract {
    fun changeText(text: String)
    fun isBusy(busy: Boolean)
    fun exposeClicks(): Observable<Unit>
}

interface HomeModelContract: BaseModelContract {
    fun accessData(): Observable<List<String>>
}