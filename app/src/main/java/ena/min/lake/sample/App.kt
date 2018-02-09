package ena.min.lake.sample

import android.app.Application
import ena.min.android.lake.Cloud
import kotlin.properties.Delegates

/**
 * Created by aminenami on 1/28/18.
 */

var appCloud: Cloud by Delegates.notNull()
    private set

var appNetworkLake: NetworkLake by Delegates.notNull()
    private set

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appCloud = Cloud()
        appNetworkLake = NetworkLake(NetworkLayer())
                .also { it.connect() }
    }
}

object Streams {
    val PERSON_SELECTED = "PERSON_SELECTED"
    val MAIN_ACTIVITY_DESTROYED = "MAIN_ACTIVITY_DESTROYED"
}
