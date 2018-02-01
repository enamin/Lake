package ena.min.omvl.sample

import android.app.Application
import android.os.Handler
import android.os.Looper
import ena.min.omvl.base.Ocean
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

/**
 * Created by aminenami on 1/28/18.
 */

var appOcean: Ocean by Delegates.notNull()
    private set

val appUiThread = Schedulers.from { Handler(Looper.getMainLooper()).post { it.run() } }


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appOcean = Ocean()
    }
}

object STREAM {
    val naviPushLake = "naviPushLake"
    val naviActiveWindow = "naviActiveWindow"
    val naviLakeStack = "naviLakeStack"
    val naviBack = "naviBack"
    val naviPop = "naviPop"
}