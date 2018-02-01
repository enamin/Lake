package ena.min.lake.sample.home

import ena.min.lake.LakeModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Created by aminenami on 1/28/18.
 */

class HomeModel : LakeModel(), HomeModelContract {
    override fun accessData(): Observable<List<String>> {
        val ps = PublishSubject.create<List<String>>()
        Thread {
            Thread.sleep(200)
            ps.onNext(listOf("Amin", "David", "Jack"))
            ps.onComplete()
        }.start()
        return ps
    }

}