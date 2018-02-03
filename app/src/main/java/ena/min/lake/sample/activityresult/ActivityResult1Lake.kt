package ena.min.lake.sample.activityresult

import ena.min.lake.EasyLake
import ena.min.lake.NO_MODEL
import ena.min.lake.Ocean
import ena.min.lake.ViewContract
import ena.min.lake.sample.Streams
import ena.min.lake.sample.appOcean
import io.reactivex.Observable

/**
 * Created by aminenami on 2/3/18.
 */

class ActivityResult1Lake(val appOcean: Ocean): EasyLake<NO_MODEL, ActivityResult1ViewContract>() {
    override fun connect(model: NO_MODEL?, view: ActivityResult1ViewContract?): ActivityResult1Lake {
        super.connect(model, view)

        (Streams.PERSON_SELECTED from appOcean) perform {
            (it as? ResultItem).let {
                it?: return@let
                view?.updateText("${it.name} : ${it.age}")
            }
        } can this

        view?.someButtonClicks perform {
            view?.startAnActivity(ActivityResultActivity2::class.java)
        } can this

        return this
    }
}

data class ResultItem(val name: String, val age: Int)

interface ActivityResult1ViewContract: ViewContract {
    val someButtonClicks: Observable<Unit>
    fun updateText(text: String)
    fun startAnActivity(clazz: Class<*>)
}