package ena.min.lake.sample.simple

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.AllInfixes
import ena.min.android.lake.DisposableCan
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_simple.*
import java.util.*

class SimpleActivity : AppCompatActivity(), DisposableCan, AllInfixes {
    override val disposables = ArrayList<Disposable?>()

    val lake = SimpleLake.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple)

        lake[SimpleLake.STREAM_UPDATE_TIMER_TEXT] perform {
            tvSimpleTime.text = it as? CharSequence?
        } can this

        lake[SimpleLake.STREAM_FINISH] perform { finish() } can this

        lake.connect()
    }

    override fun onDestroy() {
        disposeAll()
        super.onDestroy()
    }

}

