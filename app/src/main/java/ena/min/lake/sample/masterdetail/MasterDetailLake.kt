package ena.min.lake.sample.masterdetail

import ena.min.android.lake.CloudLake
import io.reactivex.Observable

/**
 * Created by aminenami on 2/16/18.
 */

class MasterDetailLake private constructor() : CloudLake() {

    val STREAM_DETAILS = streamOf<DetailModel>()
    val STREAM_MASTER_ITEM_SELECTED = streamOf<MasterListModel>()
    val STREAM_MASTER_LIST = streamOf<Iterable<MasterListModel>>()
    val STREAM_RESET_LAKE = streamOf<Unit>()

    override fun connect() {
        super.connect()

        createMasterList() sendTo STREAM_MASTER_LIST
        STREAM_MASTER_ITEM_SELECTED pipeTo ::createDetailModel pipeTo STREAM_DETAILS can this
        STREAM_RESET_LAKE thenDo {
            this.disconnect()
            instance = MasterDetailLake()
        } can this
    }

    private fun createDetailModel(masterListModel: MasterListModel): Observable<DetailModel> {
        val m = DetailModel(masterListModel.title)
        return Observable.just(m)
    }

    private fun createMasterList(): Iterable<MasterListModel> {
        return listOf(
                MasterListModel(1, "Master One"),
                MasterListModel(2, "Master 2"),
                MasterListModel(3, "Master 3"),
                MasterListModel(4, "Master 4"),
                MasterListModel(5, "Master 5"),
                MasterListModel(100, "Final Master!")
        )
    }

    companion object {
        var instance = MasterDetailLake()
            private set
    }
}


class MasterLake : CloudLake() {
    val STREAM_SHOW_LIST = streamOf<Iterable<MasterListModel>>()
    val STREAM_SELECTED_ITEM_ID = streamOf<Int>()
    val STREAM_LIST_CLICKS = streamOf<MasterListModel>()

    override fun connect() {
        super.connect()

        val mdLake = MasterDetailLake.instance

        mdLake.STREAM_MASTER_LIST.memory?.let { it sendTo STREAM_SHOW_LIST }
        mdLake.STREAM_MASTER_LIST pipeTo STREAM_SHOW_LIST can this

        mdLake.STREAM_MASTER_ITEM_SELECTED.memory?.id?.let { it sendTo STREAM_SELECTED_ITEM_ID }
        mdLake.STREAM_MASTER_ITEM_SELECTED pipeTo ::idOf pipeTo STREAM_SELECTED_ITEM_ID can this

        STREAM_LIST_CLICKS pipeTo mdLake.STREAM_MASTER_ITEM_SELECTED can this
    }

    private fun idOf(masterListModel: MasterListModel) = Observable.just(masterListModel.id)
}


class DetailLake : CloudLake() {
    val STREAM_SHOW_TEXT = streamOf<String>()

    override fun connect() {
        super.connect()

        val mdLake = MasterDetailLake.instance
        mdLake.STREAM_DETAILS.memory?.let { it.title sendTo STREAM_SHOW_TEXT }
        mdLake.STREAM_DETAILS.open()
                .map { it.title } pipeTo STREAM_SHOW_TEXT can this
    }
}