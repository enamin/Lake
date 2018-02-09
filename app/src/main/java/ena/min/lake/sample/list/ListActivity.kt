package ena.min.lake.sample.list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.AllInfixes
import ena.min.android.lake.DisposableCan
import ena.min.android.lake.appUiThread
import ena.min.lake.sample.MainAdapter
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.row_list.view.*

class ListActivity : AppCompatActivity(), AllInfixes, DisposableCan {

    override val disposables = ArrayList<Disposable?>()
    private val lake = ListLake(ListModel())
    private val items = ArrayList<ListViewModel>()

    private val adapter = object: RecyclerView.Adapter<MainAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                MainAdapter.ViewHolder(LayoutInflater.from(this@ListActivity)
                        .inflate(R.layout.row_list, parent, false))

        override fun getItemCount() = items.size

        override fun onBindViewHolder(holder: MainAdapter.ViewHolder?, position: Int) {
            val view = holder?.itemView?: return
            val item = items[position]
            view.tvTitle.text = item.title
            view.tvBody.text = item.body
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        rvList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvList.itemAnimator = DefaultItemAnimator()
        rvList.adapter = adapter

        btnRequest.setOnClickListener { Unit sendTo lake.STREAM_CLICKS }

        lake.STREAM_UPDATE_UI.open().observeOn(appUiThread) thenDo {
            btnRequest.visibility = if (it.contains(ListUiElements.BUTTON)) View.VISIBLE else View.GONE
            pbRequest.visibility = if (it.contains(ListUiElements.LOADING)) View.VISIBLE else View.GONE
            rvList.visibility = if (it.contains(ListUiElements.LIST)) View.VISIBLE else View.GONE
        } can this

        lake.STREAM_ERROR.open().observeOn(appUiThread) thenDo {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        } can this

        lake.STREAM_SHOW_ITEM.open().observeOn(appUiThread) thenDo {
            items.add(it)
            adapter.notifyItemInserted(items.size - 1)
        } can this

        lake.connect()
    }

    override fun onDestroy() {
        clearCan()
        lake.disconnect()
        super.onDestroy()
    }
}


