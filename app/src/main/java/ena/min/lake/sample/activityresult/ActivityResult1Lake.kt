package ena.min.lake.sample.activityresult

import ena.min.android.lake.Cloud
import ena.min.android.lake.CloudLake
import ena.min.android.lake.Stream

/**
 * Created by aminenami on 2/3/18.
 */

class ActivityResult1Lake : CloudLake() {

    override val cloud: Cloud
        get() = staticCloud

    companion object {
        val staticCloud = Cloud()
        val STREAM_PERSON_SELECTED = Stream<ResultItem>(staticCloud, "STREAM_PERSON_SELECTED")
    }

    val STREAM_BUTTON_CLICKS = streamOf<Unit>()
    val STREAM_UPDATE_TEXT = streamOf<String>()
    val STREAM_START_AN_ACTIVITY = streamOf<Class<*>>()

    override fun connect() {
        super.connect()

        STREAM_PERSON_SELECTED thenDoSafe  {
            "${it.name} : ${it.age}" sendTo STREAM_UPDATE_TEXT
        }

        STREAM_BUTTON_CLICKS thenDoSafe  {
            ActivityResultActivity2::class.java sendTo STREAM_START_AN_ACTIVITY
        }
    }
}

data class ResultItem(val name: String, val age: Int)