package ena.min.lake.sample.masterdetail

import android.os.Bundle
import android.view.View
import com.example.aminenami.jenkinstest.R
import kotlinx.android.synthetic.main.frag_detail.*

/**
 * Created by aminenami on 2/16/18.
 */

class DetailFrag : MasterDetailFrag(R.layout.frag_detail) {
    private val lake = DetailLake()
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lake.STREAM_SHOW_TEXT thenDo { tvDetails.text = it } can this
        lake.connect()
    }

    override fun onDetach() {
        lake.disconnect()
        clearCan()
        super.onDetach()
    }
}

