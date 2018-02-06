package ena.min.lake.sample.activityresult

import ena.min.android.lake.Cloud
import ena.min.android.lake.CloudInfix
import ena.min.lake.sample.Streams
import junit.framework.Assert
import org.junit.Test

/**
 * Created by aminenami on 2/3/18.
 */

class TestActivityResult2Lake : CloudInfix {
    private val appOcean = Cloud()
    private val lake = ActivityResult2Lake()

//    @Test
//    fun connection() {
//        val timeStamp = System.currentTimeMillis()
//
//        val view = object : ActivityResult2ViewContract {
//            override fun setSomeTexts(text: String) {
//                System.out.println("setSomeTexts: $text")
//                Assert.assertTrue(text.isNotBlank())
//                Assert.assertTrue(text.contains("Hi,"))
//                Assert.assertEquals(5, text.split(" ").size)
//            }
//
//            override fun destroyView() {
//                super.destroyView()
//                System.out.println("destory View")
//                Assert.assertTrue(timeStamp + 2000 <= System.currentTimeMillis())
//            }
//
//        }
//
//        appOcean[Streams.PERSON_SELECTED].subscribe {
//            System.out.println("inside subscriber")
//            Assert.assertTrue(it is ResultItem)
//        }
//
//        lake.connect(view = view)
//
//        Thread.sleep(3000)
//
//    }
}

