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
            val strarray = strtime?.split("-", ":", " ")
            Log.i("mytest", "month: " + strarray?.get(0))
            year = strarray?.get(0)!!.toInt()
            return year
        }

        fun TimeStrGetMonth(strtime: String?): Int {
            Log.i("mytest", "strtime: " + strtime)
            var month: Int = 10
            val strarray = strtime?.split("-", ":", " ")
            Log.i("mytest", "month: " + strarray?.get(1))
            month = strarray?.get(1)!!.toInt()

            return month
        }

        fun TimeStrGetDay(strtime: String?): Int {
            Log.i("mytest", "strtime: " + strtime)
            var day: Int = 1
            val strarray = strtime?.split("-", ":", " ")
            Log.i("mytest", "month: " + strarray?.get(2))
            day = strarray?.get(2)!!.toInt()
            return day
        }

        fun TimeStrGetHour(strtime: String?): Int {
            Log.i("mytest", "strtime: " + strtime)
            var hour: Int = 20
            val strarray = strtime?.split("-", ":", " ")
            Log.i("mytest", "month: " + strarray?.get(3))
            hour = strarray?.get(3)!!.toInt()
            return hour
        }

        fun TimeStrGetMin(strtime: String?): Int {
            var min: Int = 20
            val strarray = strtime?.split("-", ":", " ")
            Log.i("mytest", "month: " + strarray?.get(4))
            min = strarray?.get(4)!!.toInt()
            return min
        }
    }
}