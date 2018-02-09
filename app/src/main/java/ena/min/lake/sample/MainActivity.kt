package ena.min.lake.sample

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.AllInfixes
import ena.min.android.lake.DisposableCan
import ena.min.android.lake.Stream
import ena.min.lake.sample.activityresult.ActivityResultActivity1
import ena.min.lake.sample.list.ListActivity
import ena.min.lake.sample.simple.SimpleActivity
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.row_main_item.view.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), DisposableCan, AllInfixes {
    override val disposables = ArrayList<Disposable?>()
    private lateinit var mainLake: MainLake

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainLake = MainLake(MainModel())

        rvMain?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mainLake.STREAM_START_ACTIVITY thenDo {
            startActivity(Intent(this, it))
        } can this

        mainLake.STREAM_SHOW_LIST thenDo {
            val items = it?: emptyList()
            val adapter = MainAdapter(this, items)
            rvMain?.adapter = adapter
            adapter.itemClicks pipeTo mainLake.STREAM_LIST_CLICKS
        }

        mainLake.connect()

    }

    override fun onDestroy() {
        //Unsubscribe from all streams
        clearCan()
        mainLake.disconnect()
        //Just tell everyone that the main activity is destroyed (optional):
        Unit sendTo Stream<Unit>(appCloud, Streams.MAIN_ACTIVITY_DESTROYED)

        super.onDestroy()
    }
}

class MainModel : MainModelContract {

    override fun accessData(): Observable<Iterable<Item>> {
        return Observable.just(listOf(
                Item("Simple Activity", "", SimpleActivity::class.java),
                Item("Activity Result", "", ActivityResultActivity1::class.java),
                Item("a List fom Net", "", ListActivity::class.java)
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



