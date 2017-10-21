package com.txt.conference.utils

import android.util.Log

/**
 * Created by pc on 2017/10/16.
 */


class Constants {
    companion object {

        fun TimeStrGetYear(strtime: String?): Int {
            Log.i("mytest", "strtime: " + strtime)
            var year: Int = 2017
            year = strtime?.substring(0, 4)!!.toInt()
            return year
        }

        fun TimeStrGetMonth(strtime: String?): Int {
            Log.i("mytest", "strtime: " + strtime)
            var month: Int = 10
            month = strtime?.substring(5, 2)!!.toInt()
            return month
        }

        fun TimeStrGetDay(strtime: String?): Int {
            Log.i("mytest", "strtime: " + strtime)
            var day: Int = 1
            day = strtime?.substring(8, 2)!!.toInt()
            return day
        }

        fun TimeStrGetHour(strtime: String?): Int {
            Log.i("mytest", "strtime: " + strtime)
            var hour: Int = 20
            hour = strtime?.substring(11, 2)!!.toInt()
            return hour
        }

        fun TimeStrGetMin(strtime: String?): Int {
            var min: Int = 20
            min = strtime?.substring(14, 2)!!.toInt()
            return min
        }
    }
}