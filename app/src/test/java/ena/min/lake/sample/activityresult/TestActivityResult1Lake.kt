package ena.min.lake.sample.activityresult

import ena.min.lake.NO_MODEL
import ena.min.lake.Ocean
import ena.min.lake.OceanInfix
import ena.min.lake.sample.Streams
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert
import org.junit.Test

/**
 * Created by aminenami on 2/3/18.
 */

class TestActivityResult1Lake : OceanInfix {
    val appOcean = Ocean()
    val lake = ActivityResult1Lake(appOcean)

    @Test
    fun connection() {
        val view = object : ActivityResult1ViewContract {
            override val someButtonClicks = PublishSubject.create<Unit>()


            override fun updateText(text: String) {
                System.out.println("updateText was called: $text")
                Assert.assertEquals("test : 99", text)
            }

            override fun startAnActivity(clazz: Class<*>) {
                System.out.println("startAnActivity was called: $clazz")
                Assert.assertEquals(ActivityResultActivity2::class.java, clazz)
            }

        }

        lake.connect(NO_MODEL(), view)

        view.someButtonClicks.onNext(Unit)
        val testItem = ResultItem("test", 99)
        testItem sendTo appOcean via Streams.PERSON_SELECTED
    }
}

