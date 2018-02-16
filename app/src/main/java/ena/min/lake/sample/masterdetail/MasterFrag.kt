package ena.min.lake.sample.masterdetail

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aminenami.jenkinstest.R
import ena.min.lake.sample.MainAdapter
import kotlinx.android.synthetic.main.frag_master.*
import kotlinx.android.synthetic.main.row_main_item.view.*

/**
 * Created by aminenami on 2/16/18.
 */

class MasterFrag : MasterDetailFrag(R.layout.frag_master) {
    private val lake = MasterLake()
    private var selectedId = -1
        get() = lake.STREAM_SELECTED_ITEM_ID.memory ?: -1

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvMasterList.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        rvMasterList.adapter = adapter

        lake.STREAM_SHOW_LIST thenDo {
            adapter.items = it
            adapter.notifyDataSetChanged()
        } can this

        lake.STREAM_SELECTED_ITEM_ID thenDo { adapter.notifyDataSetChanged() } can this

        lake.connect()
    }

    private fun onListItemClicked(item: MasterListModel) {
        item sendTo lake.STREAM_LIST_CLICKS
    }

    override fun onDetach() {
        lake.disconnect()
        clearCan()
        super.onDetach()
    }

    private val adapter = object : RecyclerView.Adapter<MainAdapter.ViewHolder>() {
        var items: Iterable<MasterListModel>? = null
        override fun getItemCount() = items?.count() ?: 0

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                MainAdapter.ViewHolder(
                        LayoutInflater.from(this@MasterFrag.context)
                                .inflate(R.layout.row_main_item, null, false)
                )

        override fun onBindViewHolder(holder: MainAdapter.ViewHolder?, position: Int) {
            val view = holder?.itemView ?: return
            items?.elementAt(position)?.let { item ->
                view.setOnClickListener { onListItemClicked(item) }
                view.tvMainItem.text = item.title
                if (selectedId == item.id) {
                    view.ivMainItem.alpha = 1f
                    view.tvMainItem.setTextColor(Color.parseColor("#ffffff"))
                } else {
                    view.ivMainItem.alpha = 0.3f
                    view.tvMainItem.setTextColor(Color.parseColor("#33ffffff"))
                }
            }
        }
    }
}

