package com.txt.conference.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.txt.conference.R
import com.txt.conference.bean.AttendeeBean
import java.util.*
/**
 * Created by pc on 2017/10/15.
 */


class ConferenceUserAdapter(val list: List<AttendeeBean>?, private val bool_array: Array<Boolean?>, val context: Context) : BaseAdapter() {

    override fun getCount(): Int {
        return list!!.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var holder: CreateUserListViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(context, R.layout.item_chooseman, null)
            holder = CreateUserListViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as CreateUserListViewHolder
        }
        holder.usertextinfo.text = list!![position].display
        if (bool_array != null && (bool_array.size > position)) {
            if (bool_array[position] == true) {
                holder.item_bt_choose.setImageResource(R.drawable.item_corners_checked)
            } else {
                holder.item_bt_choose.setImageResource(R.drawable.item_corners)
            }
        }
        return v
    }

    override fun getItem(position: Int): Any? {
        return list!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getItemCheck(position: Int): Boolean?{
        return bool_array[position]
    }

    fun setItemCheck(position: Int, check: Boolean?){
        bool_array[position] = check
    }

    fun getCheckedNum(): Int?{
        var num = 0
        var i = 0
        while (i < bool_array.size){
            if (bool_array[i] == true){
                num++
            }
            i++
        }
        return num
    }

    fun getCheckedList(): ArrayList<AttendeeBean>? {
        var attendlist: ArrayList<AttendeeBean> = ArrayList()
        var i = 0
        while (i < bool_array.size){
            //Log.i("mytest", "i :" + i.toString())

            if (bool_array[i] == true){
                //Log.i("mytest", "i : true" )
                val attendone: AttendeeBean = list!!.get(i)
                attendlist.add(attendone)
            }
            i++
        }
        return attendlist
    }

}

class CreateUserListViewHolder(var viewItem: View) {
     var usertextinfo: TextView = viewItem.findViewById<TextView>(R.id.usertextinfo)
     var item_bt_choose: ImageView = viewItem.findViewById<ImageView>(R.id.item_bt_choose)
}
