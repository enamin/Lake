package ena.min.lake.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.specifics.NavigatorLake
import ena.min.lake.OceanInfix
import ena.min.lake.sample.home.HomeLake
import ena.min.lake.sample.home.HomeModel
import ena.min.lake.sample.home.HomeView
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates


var navigatorLake by Delegates.notNull<NavigatorLake>()
    private set

class MainActivity : AppCompatActivity(), OceanInfix {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigatorLake = NavigatorLake()

        //Create a viewless Lake to act as your App navigator
        flRoot sendTo navigatorLake via NavigatorLake.Streams.activeWindow

        //Create your first view
        val home = HomeLake().connect(HomeModel(), HomeView())
        home sendTo navigatorLake via NavigatorLake.Streams.push

        //Finish your activity when your main lake finished its job
        home.afterFlushed() perform { finish() }
    }

    override fun onBackPressed() {
        //propagate the back event to your ocean
        navigatorLake.send(NavigatorLake.Streams.back)
    }

    override fun onDestroy() {
        super.onDestroy()
        //Destroy your navigator
        navigatorLake.flush()
    }
}