package ena.min.lake.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aminenami.jenkinstest.R
import ena.min.lake.AllInfixes
import ena.min.lake.LakeModel
import ena.min.lake.LakeView
import ena.min.lake.OceanInfix
import ena.min.lake.sample.activityresult.ActivityResultActivity1
import ena.min.lake.sample.simple.SimpleActivity
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.row_main_item.view.*

class MainActivity : AppCompatActivity(), OceanInfix {
    private val mainView = MainView()
    private val mainLake = MainLake(appOcean)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainView.createView(this))
        mainLake.connect(MainModel(), mainView)
    }

    override fun onDestroy() {
        //Unsubscribe the Lake from all streams also destroy its view:
        mainLake.flush()
        //Just tell everyone that the main activity is destroyed (optional):
        Unit sendTo appOcean via Streams.MAIN_ACTIVITY_DESTROYED

        super.onDestroy()
    }
}

class MainView : LakeView(R.layout.activity_main), MainViewContract, OceanInfix {
    override fun startActivity(clazz: Class<*>) {
        val ctx = view?.context ?: return
        ctx.startActivity(Intent(ctx, clazz))
    }

    override val listClicks = PublishSubject.create<MainModel.Item>()

    override fun showList(items: Iterable<MainModel.Item>) {
        val ctx = view?.context ?: return
        view?.rvMain?.layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false)
        view?.rvMain?.adapter = MainAdapter(ctx, items).also {
            it.itemClicks pipe listClicks
        }
    }
}


class MainModel : LakeModel(), MainModelContract {

    override fun accessData(): Observable<Iterable<Item>> {
        return Observable.just(listOf(
                Item("Simple Activity", "", SimpleActivity::class.java),
                Item("Activity Result", "", ActivityResultActivity1::class.java)
        ))
    }

    data class Item(val name: String, val color: String, val destClazz: Class<*>)

}

class MainAdapter(private val context: Context, private val items: Iterable<MainModel.Item>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    val itemClicks = PublishSubject.create<MainModel.Item>()

    override fun getItemCount() = items.count()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_main_item, parent, false))
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder?, position: Int) {
        val view = holder?.itemView ?: return
        val item = items.elementAt(position)
        view.tvMainItem.text = item.name
        view.setOnClickListener {
            itemClicks.onNext(item)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}



