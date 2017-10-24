package com.txt.conference.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.txt.conference.R
import com.txt.conference.bean.AttendeeBean

/**
 * Created by jane on 2017/10/23.
 */
class InviteAdapter(layoutResId: Int, data: List<AttendeeBean>?) : BaseQuickAdapter<AttendeeBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: AttendeeBean?) {
        helper?.setBackgroundRes(R.id.item_invite_bt_choose, if (item?.invited!!) R.drawable.item_corners_checked else R.drawable.item_corners)
        helper?.setText(R.id.item_invite_tv_name, item?.display)
        helper?.addOnClickListener(R.id.item_invite_ll)
    }
}