package com.txt.conference.adapter

import android.os.CountDownTimer
import android.util.SparseArray
import android.view.View
import android.widget.TextView
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
    var timeCallBack: TimeCallBack? = null

    init {
        countDownMap = SparseArray<CountDownTimer>()
    }

    override fun convert(helper: RoomViewHolder?, item: RoomBean?) {
        helper?.setText(R.id.item_tv_room_number, item?.topic)
        helper?.getView<TextView>(R.id.item_tv_room_number)?.isSelected=true
        helper?.setText(R.id.item_tv_begin_time, String.format(TxApplication.mInstance!!.getString(R.string.begin_time_value), item?.roomNo ,DateUtils().format(item?.start, DateUtils.MM_dd_HH_mm), item?.duration, item?.creator?.display))
        helper?.addOnClickListener(R.id.item_bt_enter)
        helper?.addOnClickListener(R.id.add_attend)
        helper?.addOnClickListener(R.id.delete_button)
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
        if (room == null) {
            return
        }
        initStatus(room)
        var currenTime = Date().time
        var countDown = (room?.start!! + room.getDurationMillis() + room.getDelaytimeMillis()) - currenTime
        var countDownOffset = 5 * 60 * 1000
        var btText = ""
        var btTextColor = 0
        var bgColor = 0
        var offsetTime = 0L
//        holder?.beginEnterTime = countDown - (countDown - room.getDurationMillis())
//        holder?.beginCountDown = holder?.beginEnterTime!! + 5 * 60 * 1000
//        holder?.beginCountDown = room?.start!! + 5 * 60 * 1000
//        ULog.d(TAG, "onBindViewHolder $holder countDown:$countDown beginEnterTime:" + holder?.beginEnterTime + " beginCountDown:" + holder?.beginCountDown)
        ULog.d(TAG, "onBindViewHolder countDown: $countDown")
        if (countDown <= 0) {
            return
        }
        holder?.countDownTimer = object : CountDownTimer(countDown, 1000) {
            override fun onFinish() {
                ULog.d(TAG, "onBindViewHolder onFinish")
                timeCallBack?.onFinish()
//                mData.removeAt(holder!!.layoutPosition)
//                notifyItemRemoved(holder!!.layoutPosition)
            }

            override fun onTick(p0: Long) {
                ULog.d(TAG, "onTick $p0")
                offsetTime = room.start - Date().time
                if (offsetTime >= countDownOffset) {
                    //not begin
                    bgColor = R.drawable.enter_normal
                    btTextColor = R.color.grey_time
                    btText = mContext.getString(R.string.not_start)
                    if (room.status != RoomBean.STATUS_NORMAL) room.status = RoomBean.STATUS_NORMAL
                } else if (offsetTime > 0  && offsetTime < countDownOffset) {
                    //update countDown
                    bgColor = R.drawable.enter_countdown
                    btTextColor = android.R.color.white
                    btText = DateUtils().format(offsetTime - TimeZone.getDefault().rawOffset, DateUtils.HH_mm_ss)
                    if (room.status != RoomBean.STATUS_COUNT_DOWN) room.status = RoomBean.STATUS_COUNT_DOWN
                } else {
                    //can enter room
                    bgColor = R.drawable.enter_beging
                    btTextColor = android.R.color.white
                    btText = mContext.getString(R.string.enter_room)
                    if (room.status != RoomBean.STATUS_BEGING) room.status = RoomBean.STATUS_BEGING
                }

                holder?.setBackgroundRes(R.id.item_bt_enter, bgColor)
                holder?.setText(R.id.item_bt_enter, btText)
                holder?.setTextColor(R.id.item_bt_enter, mContext.resources.getColor(btTextColor))
            }
        }.start()
        countDownMap?.put(holder?.countDownTimer!!.hashCode(), holder.countDownTimer)
    }

    fun initStatus(room: RoomBean) {
        var countDownOffset = 5 * 60 * 1000
        var offsetTime = 0L

        offsetTime = room.start - Date().time
        if (offsetTime >= countDownOffset) {
            //not begin
            if (room.status != RoomBean.STATUS_NORMAL) room.status = RoomBean.STATUS_NORMAL
        } else if (offsetTime > 0  && offsetTime < countDownOffset) {
            //update countDown
            if (room.status != RoomBean.STATUS_COUNT_DOWN) room.status = RoomBean.STATUS_COUNT_DOWN
        } else {
            //can enter room
            if (room.status != RoomBean.STATUS_BEGING) {
                room.status = RoomBean.STATUS_BEGING
            }
        }
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

    interface TimeCallBack {
        fun onFinish()
    }
}