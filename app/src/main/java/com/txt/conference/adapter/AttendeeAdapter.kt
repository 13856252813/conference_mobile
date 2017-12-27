package com.txt.conference.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.txt.conference.R
import com.txt.conference.bean.AttendeeBean

/**
 * Created by jane on 2017/10/20.
 */
class AttendeeAdapter(layoutResId: Int, data: List<AttendeeBean>?) : BaseQuickAdapter<AttendeeBean, BaseViewHolder>(layoutResId, data) {
    var creatorName: String? = null
    var selfName: String? = null
    var isCreator:Boolean=false

   public  fun isCreator(isCreator:Boolean){
        this.isCreator=isCreator
    }

    override fun convert(helper: BaseViewHolder?, item: AttendeeBean?) {

        if (item?.display.equals(creatorName)) {
            helper?.setGone(R.id.item_attendee_iv_role, true)
            helper?.setGone(R.id.media_control, false)
        } else{
            helper?.setGone(R.id.item_attendee_iv_role, false)
            if(isCreator){
                helper?.setGone(R.id.media_control, true)
            }else{
                helper?.setGone(R.id.media_control, false)
            }
        }

//        if(isCreator){
//            helper?.setGone(R.id.media_control, true)
//        }else{
//            helper?.setGone(R.id.media_control, false)
//        }

        if (item?.display.equals(selfName)){
            helper?.setTextColor(R.id.item_attendee_name, mContext.resources.getColor(R.color.orange))
        } else {
            helper?.setTextColor(R.id.item_attendee_name, mContext.resources.getColor(R.color.white))
        }
        if (item?.videoMute.equals("0")){
            helper?.setImageResource(R.id.item_attendee_iv_vedio, R.mipmap.addtend_vedio)
        } else {
            helper?.setImageResource(R.id.item_attendee_iv_vedio, R.mipmap.addtend_vedio_push)
        }
        if (item?.audioMute.equals("0")){
            helper?.setImageResource(R.id.item_attendee_iv_sound, R.mipmap.addtend_voice)
        } else {
            helper?.setImageResource(R.id.item_attendee_iv_sound, R.mipmap.addtend_voice_push)
        }
        helper?.addOnClickListener(R.id.item_attendee_iv_vedio)
        helper?.addOnClickListener(R.id.item_attendee_iv_sound)
        helper?.addOnClickListener(R.id.item_attendee_iv_more)
        helper?.setText(R.id.item_attendee_name, item?.display)
    }
}