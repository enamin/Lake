package ena.min.lake.sample.masterdetail

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.AllInfixes
import ena.min.android.lake.DisposableCan
import io.reactivex.disposables.Disposable
import java.util.*

class MasterDetailActivity : AppCompatActivity(), DisposableCan, AllInfixes {
    override val disposables = ArrayList<Disposable?>()

    private val lake = MasterDetailLake.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_master_detail)

        val transaction = supportFragmentManager.beginTransaction()

        if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) {
            transaction.add(R.id.vgMaster, MasterFrag())
            transaction.add(R.id.vgDetail, DetailFrag())
            transaction.commit()
        } else {
            transaction.add(R.id.vgContainer, MasterFrag())
            transaction.commit()

            lake.STREAM_DETAILS.memory?.let { showDetails() }
            lake.STREAM_DETAILS thenDo { showDetails() } can this
        }


        if (!lake.isConnected) {
            lake.connect()
        }
    }

    private fun showDetails() {
        supportFragmentManager.findFragmentByTag("detail")?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }

        supportFragmentManager.beginTransaction()
                .add(R.id.vgContainer, DetailFrag(), "detail")
                .commit()

    }

    override fun onBackPressed() {
        if (resources.configuration.orientation == ORIENTATION_LANDSCAPE) {
            finish()
            return
        }

        val frag = supportFragmentManager.findFragmentByTag("detail")
        if (frag != null) {
            supportFragmentManager.beginTransaction().remove(frag).commit()
        } else {
            super.onBackPressed()
        }

    }

    override fun onDestroy() {
        clearCan()
        super.onDestroy()
    }
}


abstract class MasterDetailFrag(private val resId: Int) : Fragment(), DisposableCan, AllInfixes {
    override val disposables = ArrayList<Disposable?>()
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(resId, container, false)
    }
}