package ena.min.lake.sample.activityresult

import ena.min.android.lake.Cloud
import ena.min.android.lake.EasyLake
import ena.min.android.lake.Stream

/**
 * Created by aminenami on 2/3/18.
 */

class ActivityResult1Lake : EasyLake() {

    companion object {
        val cloud = Cloud()
        val STREAM_PERSON_SELECTED = Stream<ResultItem>(cloud, "STREAM_PERSON_SELECTED")
    }

    val STREAM_BUTTON_CLICKS = Stream<Unit>(cloud, "STREAM_BUTTON_CLICKS")
    val STREAM_UPDATE_TEXT = Stream<String>(cloud, "STREAM_UPDATE_TEXT")
    val STREAM_START_AN_ACTIVITY = Stream<Class<*>>(cloud, "STREAM_START_AN_ACTIVITY")


    override fun connect() {
        super.connect()

        STREAM_PERSON_SELECTED thenDo {
            "${it?.name} : ${it?.age}" sendTo STREAM_UPDATE_TEXT
        } can this

        STREAM_BUTTON_CLICKS thenDo {
            ActivityResultActivity2::class.java sendTo STREAM_START_AN_ACTIVITY
        } can this
    }
}

data class ResultItem(val name: String, val age: Int)