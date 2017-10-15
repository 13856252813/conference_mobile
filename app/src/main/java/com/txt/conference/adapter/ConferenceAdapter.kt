package com.txt.conference.adapter

import android.os.CountDownTimer
import android.util.SparseArray
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.common.utlis.DateUtils
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.RoomBean
import java.util.*


/**
 * Created by jane on 2017/10/11.
 */
class ConferenceAdapter(layoutResId: Int, data: List<RoomBean>?) : BaseQuickAdapter<RoomBean, ConferenceAdapter.RoomViewHolder>(layoutResId, data) {
    var TAG = ConferenceAdapter::class.java.simpleName
    var countDownMap: SparseArray<CountDownTimer>? = null

    init {
        countDownMap = SparseArray<CountDownTimer>()
    }

    override fun convert(helper: RoomViewHolder?, item: RoomBean?) {
        helper?.setText(R.id.item_tv_room_number, item?.roomNo)
        helper?.setText(R.id.item_tv_begin_time, String.format(TxApplication.mInstance!!.getString(R.string.begin_time_value), DateUtils().format(item?.start, DateUtils.MM_dd_HH_mm), item?.duration, item?.creator?.display))
        helper?.addOnClickListener(R.id.item_bt_enter)
//        var bgColor = 0
//        when (item?.status) {
//            RoomBean.STATUS_NORMAL -> bgColor = R.color.grey_time
//            RoomBean.STATUS_BEGING -> bgColor = R.color.enter
//            RoomBean.STATUS_COUNT_DOWN -> bgColor = R.color.grey_time
//        }
    }

    override fun onBindViewHolder(holder: RoomViewHolder?, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder?.countDownTimer != null) {
            holder?.countDownTimer?.cancel()
        }
        var room = getItem(position)
        var currenTime = Date().time
        var countDown = (room?.start!! + room.getDurationMillis()) - currenTime
        var countDownOffset = 5 * 60 * 1000
        var btText = ""
        var btTextColor = 0
        var bgColor = 0
        var offsetTime = 0L
//        holder?.beginEnterTime = countDown - (countDown - room.getDurationMillis())
//        holder?.beginCountDown = holder?.beginEnterTime!! + 5 * 60 * 1000
//        holder?.beginCountDown = room?.start!! + 5 * 60 * 1000
//        ULog.d(TAG, "onBindViewHolder $holder countDown:$countDown beginEnterTime:" + holder?.beginEnterTime + " beginCountDown:" + holder?.beginCountDown)
        ULog.d(TAG, "onBindViewHolder $holder")
        holder?.countDownTimer = object : CountDownTimer(countDown, 1000) {
            override fun onFinish() {
                mData.removeAt(holder!!.layoutPosition)
                notifyItemRemoved(holder!!.layoutPosition)
            }

            override fun onTick(p0: Long) {
                ULog.d(TAG, "onTick $p0")
//                if (holder!!.beginCountDown > 0 && p0 > holder!!.beginCountDown) {
//                    //not begin
//                    bgColor = R.drawable.enter_normal
//                    btText = mContext.getString(R.string.not_start)
//                } else if ((holder?.beginCountDown > 0 && p0 <= holder?.beginCountDown) && (holder?.beginEnterTime > 0 && p0 > holder?.beginEnterTime)) {
//                    //update countDown
//                    bgColor = R.drawable.enter_countdown
//                    btText = DateUtils().format(p0 - holder.beginEnterTime - TimeZone.getDefault().rawOffset, DateUtils.HH_mm_ss)
//                } else {
//                    //can enter room
//                    bgColor = R.drawable.enter_beging
//                    btText = mContext.getString(R.string.enter_room)
//                }

                offsetTime = room.start - Date().time
                if (offsetTime >= countDownOffset) {
                    //not begin
                    bgColor = R.drawable.enter_normal
                    btTextColor = R.color.grey_time
                    btText = mContext.getString(R.string.not_start)
                } else if (offsetTime > 0  && offsetTime < countDownOffset) {
                    //update countDown
                    bgColor = R.drawable.enter_countdown
                    btTextColor = android.R.color.white
                    btText = DateUtils().format(offsetTime - TimeZone.getDefault().rawOffset, DateUtils.HH_mm_ss)
                } else {
                    //can enter room
                    bgColor = R.drawable.enter_beging
                    btTextColor = android.R.color.white
                    btText = mContext.getString(R.string.enter_room)
                }

                holder?.setBackgroundRes(R.id.item_bt_enter, bgColor)
                holder?.setText(R.id.item_bt_enter, btText)
                holder?.setTextColor(R.id.item_bt_enter, mContext.resources.getColor(btTextColor))
            }
        }.start()
        countDownMap?.put(holder?.countDownTimer!!.hashCode(), holder.countDownTimer)
    }

    fun cancelAllTimers() {
        if (countDownMap == null) {
            return
        }
        for (i in 0..countDownMap!!.size()-1) {
            countDownMap?.get(countDownMap!!.keyAt(i))?.cancel()
        }
    }

    override fun createBaseViewHolder(view: View?): RoomViewHolder {
        return RoomViewHolder(view)
    }

    class RoomViewHolder(view: View?) : BaseViewHolder(view) {
        var countDownTimer: CountDownTimer? = null
//        var beginEnterTime: Long = 0L
//        var beginCountDown: Long = 0L
    }
}