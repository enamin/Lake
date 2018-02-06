package ena.min.lake

import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert
import org.junit.Test

/**
 * Created by aminenami on 2/6/18.
 */

class GeneralTests {
    @Test
    fun behaviorSubject() {
        val bs = BehaviorSubject.create<Int>()
        bs.onNext(1)
        bs.onNext(2)
        bs.onNext(3)

        bs.subscribe {
            Assert.assertEquals(3, it)
        }
    }

    @Test
    fun publishSubject() {
        val ps = PublishSubject.create<Int>()

        ps.subscribe {
            Assert.assertEquals(1, it)
        }

        ps.onNext(1)

        ps.subscribe {
            throw Exception("this should not be raised!")
        }
    }


}