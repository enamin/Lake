package ena.min.omvl.sample.home

import ena.min.omvl.base.InfixBaseLake
import ena.min.omvl.base.BaseLakeCreator
import ena.min.omvl.sample.STREAM
import ena.min.omvl.sample.appOcean
import ena.min.omvl.sample.appUiThread
import ena.min.omvl.sample.profile.ProfileLake
import ena.min.omvl.sample.profile.ProfileView
import ena.min.omvl.util.watchTapOnFor

/**
 * Created by aminenami on 1/28/18.
 */

class HomeLake : InfixBaseLake(), BaseLakeCreator<HomeModelContract, HomeViewContract> {

    private var model: HomeModelContract? = null
    private var view: HomeViewContract? = null
    private var aLocalState = 0
    private var isBusy_anotherLocalState = false
        set(value) {
            if (field != value) {
                view?.isBusy(value)
                field = value
            }
        }

    override fun create(model: HomeModelContract?, view: HomeViewContract?): HomeLake {
        if (model == null || view == null) throw IllegalArgumentException("model or view is null")
        this.model = model
        this.view = view
        initialize()
        return this
    }

    private fun initialize() {
        appOcean["user"].repeat() act { view?.changeText("The UserName: $it") } can this

        view?.exposeClicks() act {
            if (isBusy_anotherLocalState) {
                return@act
            }

            changeLocalState(aLocalState + 1)
        } can this

        watchTapOnFor(this) act {
            if (aLocalState > 0) {
                changeLocalState(aLocalState - 1)
            } else {
                appOcean.emit(STREAM.naviPop, this)
            }
        } can this
    }

    private fun changeLocalState(newValue: Int) {
        aLocalState = newValue
        view?.changeText("$aLocalState | $aLocalState")

        if (aLocalState == 3) {
            isBusy_anotherLocalState = true
            model?.accessData()?.observeOn(appUiThread)?.subscribe {
                view?.changeText(it.reduce { s, acc -> acc + '\n' + s })
                isBusy_anotherLocalState = false
            }
        }

        if (aLocalState == 5) {
            val pi = ProfileLake().create(null, ProfileView())
            appOcean.emit(STREAM.naviPushLake, pi)
        }
    }

    override fun getViewContract() = view

}