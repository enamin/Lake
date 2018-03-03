package ena.min.lake.sample.simple

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.AllInfixes
import ena.min.android.lake.Bin
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_simple.*
import java.util.*

class SimpleActivity : AppCompatActivity(), Bin, AllInfixes {
    override val disposables = ArrayList<Disposable?>()

    val lake = SimpleLake.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        lake.STREAM_UPDATE_TIMER_TEXT thenDo {
            tvSimpleTime.text = it
        }

        lake.STREAM_FINISH thenDo { finish() }

        if (!lake.isConnected) {
            lake.connect()
        }
    }

    override fun onDestroy() {
        clearBin()
        super.onDestroy()
    }

}

