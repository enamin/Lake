package ena.min.android.lake.specifics.navigator

import ena.min.lake.ViewContract

/**
 * Created by aminenami on 2/2/18.
 */

interface NavigatorViewContract : ViewContract {
    /**
    * @return true if you successfully showed the view or false if you refused to show it.
     * */
    fun navigationShowView(view: ViewContract?): Boolean
}