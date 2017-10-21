package com.txt.conference.utils

import android.util.Log

/**
 * Created by pc on 2017/10/16.
 */


class Constants {
    companion object {

        fun TimeStrGetYear(strtime: String?): Int {
            var year: Int = 2017
            val strarray = strtime?.split("-", ":", " ")
            year = strarray?.get(0)!!.toInt()
            return year
        }

        fun TimeStrGetMonth(strtime: String?): Int {
            var month: Int = 10
            val strarray = strtime?.split("-", ":", " ")
            month = strarray?.get(1)!!.toInt()

            return month
        }

        fun TimeStrGetDay(strtime: String?): Int {
            var day: Int = 1
            val strarray = strtime?.split("-", ":", " ")
            day = strarray?.get(2)!!.toInt()
            return day
        }

        fun TimeStrGetHour(strtime: String?): Int {
            var hour: Int = 20
            val strarray = strtime?.split("-", ":", " ")
            hour = strarray?.get(3)!!.toInt()
            return hour
        }

        fun TimeStrGetMin(strtime: String?): Int {
            var min: Int = 20
            val strarray = strtime?.split("-", ":", " ")
            min = strarray?.get(4)!!.toInt()
            return min
        }
    }
}