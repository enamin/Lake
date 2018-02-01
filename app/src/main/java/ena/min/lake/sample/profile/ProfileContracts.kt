package ena.min.lake.sample.profile

import ena.min.lake.ViewContract
import io.reactivex.Observable

/**
 * Created by aminenami on 1/29/18.
 */

interface ProfileViewContract : ViewContract {
    fun exposeClicks(): Observable<Unit>
}
