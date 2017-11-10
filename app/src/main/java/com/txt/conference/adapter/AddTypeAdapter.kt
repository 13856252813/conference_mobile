package com.txt.conference.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.txt.conference.R
import com.txt.conference.bean.AddTypeBean
import com.txt.conference.bean.AttendeeBean

/**
 * Created by jane on 2017/11/10.
 */
class AddTypeAdapter(layoutResId: Int, data: List<AddTypeBean>?) : BaseQuickAdapter<AddTypeBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: AddTypeBean?) {

        helper?.setBackgroundRes(R.id.item_addtype_bt_choose, item?.icon!!)
        helper?.setText(R.id.item_addtype_tv_name, item?.strinfo)
        helper?.addOnClickListener(R.id.item_addtype_ll)
    }
}