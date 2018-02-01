package ena.min.lake.sample.home

import ena.min.android.lake.specifics.NavigatorLake
import ena.min.lake.InfixLake
import ena.min.lake.NO_MODEL
import ena.min.lake.appUiThread
import ena.min.lake.sample.appOcean
import ena.min.lake.sample.navigatorLake
import ena.min.lake.sample.profile.ProfileLake
import ena.min.lake.sample.profile.ProfileView

/**
 * Created by aminenami on 1/28/18.
 */

class HomeLake : InfixLake<HomeModelContract, HomeViewContract>() {
    private var aLocalState = 0
    private var isBusy_anotherLocalState = false
        set(value) {
            if (field != value) {
                view.isBusy(value)
                field = value
            }
        }

    override fun connect(model: HomeModelContract, view: HomeViewContract): HomeLake {
        super.connect(model, view)
        appOcean["user"].repeat() perform { view.changeText("The UserName: $it") } can this

        view.exposeClicks() perform {
            if (isBusy_anotherLocalState) {
                return@perform
            }

            changeLocalState(aLocalState + 1)
        } can this

        navigatorLake.watchTopOnBackFor(this) perform {
            if (aLocalState > 0) {
                changeLocalState(aLocalState - 1)
            } else {
                navigatorLake.send(NavigatorLake.Streams.pop, this)
            }
        } can this

        return this
    }

    private fun changeLocalState(newValue: Int) {
        aLocalState = newValue
        view.changeText("$aLocalState | $aLocalState")

        if (aLocalState == 3) {
            isBusy_anotherLocalState = true
            model.accessData().observeOn(appUiThread)?.subscribe {
                view.changeText(it.reduce { s, acc -> acc + '\n' + s })
                isBusy_anotherLocalState = false
            }
        }

        if (aLocalState == 5) {
            val pi = ProfileLake().connect(NO_MODEL(), ProfileView())
            pi sendTo navigatorLake via NavigatorLake.Streams.push
        }
    }
}