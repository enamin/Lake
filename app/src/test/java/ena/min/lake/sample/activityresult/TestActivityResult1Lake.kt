package ena.min.lake.sample.activityresult

import ena.min.android.lake.CloudInfix
import junit.framework.Assert
import org.junit.Test

/**
 * Created by aminenami on 2/3/18.
 */

class TestActivityResult1Lake : CloudInfix {
    private val lake = ActivityResult1Lake()

    @Test
    fun connect() {

        lake.connect()

        val resultItem = ResultItem("test", -1)
        resultItem sendTo ActivityResult1Lake.STREAM_PERSON_SELECTED

        lake.STREAM_UPDATE_TEXT unsafeThenDo {
            Assert.assertEquals("${resultItem.name} : ${resultItem.age}", it)
        }

        Unit sendTo lake.STREAM_BUTTON_CLICKS

        lake.STREAM_START_AN_ACTIVITY unsafeThenDo {
            Assert.assertEquals(ActivityResultActivity2::class.java, it)
        }

    }
}

