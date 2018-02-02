package ena.min.lake.sample.navigator

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.aminenami.jenkinstest.R
import ena.min.android.lake.specifics.navigator.NavigatorLake
import ena.min.android.lake.specifics.navigator.NavigatorViewContract
import ena.min.lake.OceanInfix
import ena.min.lake.ViewContract
import ena.min.lake.sample.navigator.home.HomeLake
import ena.min.lake.sample.navigator.home.HomeModel
import ena.min.lake.sample.navigator.home.HomeView
import kotlinx.android.synthetic.main.activity_navigator.*
import kotlin.properties.Delegates


var navigatorLake by Delegates.notNull<NavigatorLake>()
    private set

class NavigatorActivity : AppCompatActivity(), NavigatorViewContract, OceanInfix {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigator)

        //initialize navigator lake:
        navigatorLake = NavigatorLake().connect(view = this)

        //Create your first view
        val home = HomeLake().connect(HomeModel(), HomeView())
        home sendTo navigatorLake via NavigatorLake.Streams.push

        //Finish your activity when your main lake finished its job
        home.afterFlushed() perform { finish() }
    }

    override fun navigationShowView(view: ViewContract?): Boolean {
        if (isFinishing) {
            return false
        }

        view?: return false

        val v = view.createView(this)?: return false

        flRoot.addView(v)
        return true
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