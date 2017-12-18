package com.txt.conference.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.txt.conference.R
import com.txt.conference.bean.AttendeeBean
import kotlinx.android.synthetic.main.item_attendee.*

/**
 * Created by jane on 2017/10/20.
 */
class AttendeeAdapter(layoutResId: Int, data: List<AttendeeBean>?) : BaseQuickAdapter<AttendeeBean, BaseViewHolder>(layoutResId, data) {
    var creatorName: String? = null
    var selfName: String? = null

    override fun convert(helper: BaseViewHolder?, item: AttendeeBean?) {
        if (item?.display.equals(creatorName)) {
            helper?.setVisible(R.id.item_attendee_iv_role, true)
        } else{
            helper?.setVisible(R.id.item_attendee_iv_role, false)
        }

        if (item?.display.equals(selfName)){
            helper?.setTextColor(R.id.item_attendee_name, mContext.resources.getColor(R.color.orange))
        } else {
            helper?.setTextColor(R.id.item_attendee_name, mContext.resources.getColor(R.color.white))
        }

        //if (item?.audioMute.equals("0")){
        //    helper?.setImageResource(R.id.item_attendee_iv_vedio, R.mipmap.atte)
        //}
        helper?.addOnClickListener(R.id.item_attendee_iv_vedio)
        helper?.addOnClickListener(R.id.item_attendee_iv_sound)
        helper?.addOnClickListener(R.id.item_attendee_iv_more)

        helper?.setText(R.id.item_attendee_name, item?.display)
    }
}