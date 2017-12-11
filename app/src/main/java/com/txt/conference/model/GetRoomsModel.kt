package com.txt.conference.model

import com.common.http.HttpEventHandler
import com.common.utlis.DateUtils
import com.common.utlis.ULog
import com.txt.conference.bean.GetRoomBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.http.GetRoomsHttpFactory
import java.util.*

/**
 * Created by jane on 2017/10/13.
 */
class GetRoomsModel : IGetRoomsModel {

    private val TAG: String = "GetRoomsModel"
    override var status: Int = Status.FAILED
    override var msg: String? = null
    override var rooms: List<RoomBean>? = null

    private var getRoomsHttp: GetRoomsHttpFactory? = null

    fun deleteTheEndRooms(roomlist: List<RoomBean>?): List<RoomBean> {
        var resultrooms: ArrayList<RoomBean>
        var date= DateUtils()
        var offsetTime = 0L
        var currentTime = DateUtils().format(Date().time, DateUtils.yyyy_MM_dd__HH_mm_ss)
        var durationTime = 0L
        var strOffset = ""
        var endTime = ""
        resultrooms = ArrayList<RoomBean>()
        for (i in 0..roomlist!!.size - 1){
            durationTime = roomlist.get(i).duration * 1000L * 60L * 60L + roomlist.get(i).delaytime * 1000L * 60L
            endTime = DateUtils().format(roomlist.get(i).start + durationTime, DateUtils.yyyy_MM_dd__HH_mm_ss)
            ULog.i(TAG, "currentTime: " + currentTime + " endTime: " + endTime)
            if (endTime.compareTo(currentTime) > 0){
                resultrooms.add(roomlist.get(i))
            }
        }
        return resultrooms
    }

    override fun loadRooms(token: String, callBack: IBaseModel.IModelCallBack) {
        if (getRoomsHttp == null) {
            getRoomsHttp = GetRoomsHttpFactory()
            getRoomsHttp?.setHttpEventHandler(object : HttpEventHandler<GetRoomBean>() {
                override fun HttpSucessHandler(result: GetRoomBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {
                        rooms = deleteTheEndRooms(result?.data)
                    } else {
                        rooms = null
                        msg = result?.msg
                    }
                    callBack.onStatus()
                }

                override fun HttpFailHandler() {
                    status = Status.FAILED_UNKNOW
                    callBack.onStatus()
                }
            })
        }
        getRoomsHttp?.DownloaDatas(token)
    }
}