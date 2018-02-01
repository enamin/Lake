package ena.min.omvl.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.omvl.sample.home.HomeLake
import ena.min.omvl.sample.home.HomeModel
import ena.min.omvl.sample.home.HomeView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var naviLake: NaviLake? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Create a viewless Lake to act as your App navigator
        naviLake = NaviLake()
        appOcean.emit(STREAM.naviActiveWindow, flRoot)

        //Create your first view
        val home = HomeLake().create(HomeModel(), HomeView())
        appOcean.emit(STREAM.naviPushLake, home)

        //Finish your activity when your main lake finished its job
        home.afterFlushed().subscribe { finish() }
    }

    override fun onBackPressed() {
        //propagate the back event to your ocean
        appOcean.emit(STREAM.naviBack, Unit)
    }

    override fun onDestroy() {
        super.onDestroy()
        //Destroy your navigator
        naviLake?.flush()
    }
}