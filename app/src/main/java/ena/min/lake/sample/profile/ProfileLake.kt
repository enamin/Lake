package ena.min.lake.sample.profile

import ena.min.android.lake.specifics.NavigatorLake
import ena.min.lake.InfixLake
import ena.min.lake.NO_MODEL
import ena.min.lake.sample.appOcean
import ena.min.lake.sample.navigatorLake

/**
 * Created by aminenami on 1/29/18.
 */

class ProfileLake : InfixLake<NO_MODEL, ProfileViewContract>() {

    override fun connect(model: NO_MODEL, view: ProfileViewContract): ProfileLake {
        super.connect(model, view)
        view.exposeClicks().subscribe {
            appOcean.send("user", "Devil!!!!!")
        } can this

        initialize()
        return this
    }


    private fun initialize() {
        navigatorLake.watchTopOnBackFor(this)?.subscribe {
            navigatorLake.send(NavigatorLake.Streams.pop, this)
        } can this
    }
}