package ena.min.lake.sample.home

import ena.min.lake.ModelContract
import ena.min.lake.ViewContract
import io.reactivex.Observable

/**
 * Created by aminenami on 1/28/18.
 */

interface HomeViewContract : ViewContract {
    fun changeText(text: String)
    fun isBusy(busy: Boolean)
    fun exposeClicks(): Observable<Unit>
}

interface HomeModelContract : ModelContract {
    fun accessData(): Observable<List<String>>
}