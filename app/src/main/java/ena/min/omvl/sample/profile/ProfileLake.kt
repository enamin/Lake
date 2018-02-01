package ena.min.omvl.sample.profile

import ena.min.omvl.base.InfixBaseLake
import ena.min.omvl.base.BaseLakeCreator
import ena.min.omvl.base.NO_MODEL
import ena.min.omvl.sample.STREAM
import ena.min.omvl.sample.appOcean
import ena.min.omvl.util.watchTapOnFor

/**
 * Created by aminenami on 1/29/18.
 */

class ProfileLake : InfixBaseLake(), BaseLakeCreator<NO_MODEL, ProfileViewContract> {
    override fun getViewContract(): ProfileViewContract? {
        return view
    }

    private var view: ProfileViewContract? = null

    override fun create(model: NO_MODEL?, view: ProfileViewContract?): ProfileLake {
        this.view = view
        view?.exposeClicks()?.subscribe {
            appOcean.emit("user", "Devil!!!!!")
        } can this

        initialize()

        return this
    }

    private fun initialize() {
        watchTapOnFor(this)?.subscribe {
            appOcean.emit(STREAM.naviPop, this)
        } can this
    }
}