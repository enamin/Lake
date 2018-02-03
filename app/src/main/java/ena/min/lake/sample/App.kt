package ena.min.lake.sample

import android.app.Application
import ena.min.lake.Ocean
import kotlin.properties.Delegates

/**
 * Created by aminenami on 1/28/18.
 */

var appOcean: Ocean by Delegates.notNull()
    private set

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        appOcean = Ocean()
    }
}

object Streams {
    val PERSON_SELECTED = "PERSON_SELECTED"
    val MAIN_ACTIVITY_DESTROYED = "MAIN_ACTIVITY_DESTROYED"
}
