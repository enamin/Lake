package ena.min.omvl.sample.home

import android.content.Context
import android.view.View
import ena.min.omvl.base.BaseView
import com.example.aminenami.jenkinstest.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_home.view.*

/**
 * Created by aminenami on 1/28/18.
 */

class HomeView : BaseView(R.layout.view_home), HomeViewContract {
    private val clicks = PublishSubject.create<Unit>()

    override fun createView(context: Context): View? {
        super<BaseView>.createView(context)
        view?.btnClickMe?.setOnClickListener { clicks.onNext(Unit) }
        return view
    }

    override fun changeText(text: String) {
        view?.tvHome?.text = text
    }

    override fun isBusy(busy: Boolean) {
        view?.vgLoading?.visibility = if (busy) View.VISIBLE else View.GONE
    }

    override fun exposeClicks(): Observable<Unit> {
        return clicks
    }

}