package ena.min.lake.sample.profile

import android.content.Context
import android.view.View
import com.example.aminenami.jenkinstest.R
import ena.min.lake.LakeView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_profile.view.*

/**
 * Created by aminenami on 1/29/18.
 */

class ProfileView : LakeView(R.layout.view_profile), ProfileViewContract {
    private val clicks = PublishSubject.create<Unit>()

    override fun exposeClicks(): Observable<Unit> {
        return clicks
    }

    override fun createView(context: Context): View? {
        super<LakeView>.createView(context)
        view?.btnEvil?.setOnClickListener { clicks.onNext(Unit) }
        return view
    }
}