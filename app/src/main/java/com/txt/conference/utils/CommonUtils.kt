package com.txt.conference.utils
  
import android.content.Context
import android.content.Intent
import android.net.Uri

import com.common.utlis.DateUtils
import com.common.utlis.ULog
import com.txt.conference.R

import com.txt.conference.bean.RoomBean
import com.txt.conference.http.Urls

/**
 * Created by pc on 2017/11/23.
 */

class CommonUtils {


        companion object {
            var TAG = "CommonUtils"

            fun startSendSms(context: Context, room: RoomBean){
                var date= DateUtils()
                ULog.i(TAG, "startSendSms" )
                var smsToUri = Uri.parse("smsto:")
                var intent = Intent(Intent.ACTION_SENDTO, smsToUri)
                var str_sms_Message = String.format(context.getString(R.string.sms_message), room?.creator?.display,
                        date.format(room?.start,DateUtils.HH_mm), room?.roomNo, Urls.HOST)
                intent.putExtra("sms_body", str_sms_Message)
                context.startActivity(intent)
            }
        }
    }