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

        bs.skip(1).subscribe {
            System.out.println("Hi!!")
            Assert.assertEquals(4, it)
        }

        bs.onNext(4)


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