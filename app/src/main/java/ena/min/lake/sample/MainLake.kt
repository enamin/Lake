package ena.min.lake.sample

import ena.min.lake.EasyLake
import ena.min.lake.ModelContract
import ena.min.lake.Ocean
import ena.min.lake.ViewContract
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by aminenami on 2/3/18.
 */
class MainLake(val appOcean: Ocean) : EasyLake<MainModelContract, MainViewContract>() {
    override fun connect(model: MainModelContract?, view: MainViewContract?): MainLake {
        super.connect(model, view)

        view?.listClicks?.delay(300, TimeUnit.MILLISECONDS)
                ?.subscribe { view?.startActivity(it.destClazz) } can this

        model?.accessData() perform { view?.showList(it?: emptyList()) }
        return this
    }
}

interface MainModelContract : ModelContract {
    fun accessData(): Observable<Iterable<MainModel.Item>>
}

interface MainViewContract : ViewContract {
    val listClicks: Observable<MainModel.Item>
    fun showList(items: Iterable<MainModel.Item>)
    fun startActivity(clazz: Class<*>)
}