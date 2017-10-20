package com.txt.conference.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.txt.conference.R
import com.txt.conference.bean.AttendeeBean
import kotlinx.android.synthetic.main.item_attendee.*

/**
 * Created by jane on 2017/10/20.
 */
class AttendeeAdapter(layoutResId: Int, data: List<AttendeeBean>?) : BaseQuickAdapter<AttendeeBean, BaseViewHolder>(layoutResId, data) {

    override fun convert(helper: BaseViewHolder?, item: AttendeeBean?) {
        helper?.setText(R.id.item_attendee_name, item?.display)
    }
}