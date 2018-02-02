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
import ena.min.lake.sample.activityresult.ActivityResultActivity1
import ena.min.lake.sample.simple.SimpleActivity
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.row_main_item.view.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainItems = listOf(
                MainItem("Simple Activity", "", Intent(this, SimpleActivity::class.java)),
                MainItem("Activity Result", "", Intent(this, ActivityResultActivity1::class.java))
        )

        rvMain.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvMain.adapter = MainAdapter(this, mainItems).also {
            it.itemClicks
                    .delay(300, TimeUnit.MILLISECONDS)
                    .subscribe { startActivity(it.intent) }
        }
    }
}

class MainAdapter(private val context: Context, private val items: List<MainItem>) : RecyclerView.Adapter<ViewHolder>() {
    val itemClicks = PublishSubject.create<MainItem>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.row_main_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val view = holder?.itemView ?: return
        val item = items[position]
        view.tvMainItem.text = item.name
        view.setOnClickListener {
            itemClicks.onNext(item)
        }
    }

}

data class MainItem(val name: String, val color: String, val intent: Intent)

class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
