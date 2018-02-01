package ena.min.omvl.sample.profile

import ena.min.omvl.base.BaseViewContract
import io.reactivex.Observable

/**
 * Created by aminenami on 1/29/18.
 */

interface ProfileViewContract: BaseViewContract {
    fun exposeClicks(): Observable<Unit>
}
