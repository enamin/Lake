package ena.min.lake.sample

import android.app.Application
import android.os.Handler
import android.os.Looper
import ena.min.android.lake.specifics.NavigatorLake
import ena.min.lake.Ocean
import io.reactivex.schedulers.Schedulers
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
