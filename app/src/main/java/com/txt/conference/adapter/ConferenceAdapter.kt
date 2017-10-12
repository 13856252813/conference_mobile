package com.txt.conference.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.txt.conference.R


/**
 * Created by jane on 2017/10/11.
 */
class ConferenceAdapter(layoutResId: Int, data: MutableList<String>?) : BaseQuickAdapter<String, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.item_tv_room_number, item)
    }

}