package com.txt.conference.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.ImageView
import java.util.*
import com.txt.conference.R

import com.txt.conference.bean.CreateRoomListAdapterBean
/**
 * Created by pc on 2017/10/15.
 */


class CreateRoomListAdapter(val list: ArrayList<CreateRoomListAdapterBean>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var holder: CreateRoomListViewHolder
        var v: View
        if (convertView == null) {
            v = View.inflate(context, R.layout.item_createroom, null)
            holder = CreateRoomListViewHolder(v)
            v.tag = holder
        } else {
            v = convertView
            holder = v.tag as CreateRoomListViewHolder
        }
        holder.textinfo.text = list[position].strinfo
        holder.textinfo2.text = list[position].strinfo2
        holder.imageView1.setImageResource(list[position].icon)
        holder.imageView2.setImageResource(list[position].icon2)

        if (position == 0){
            holder.imageView2.setVisibility(View.GONE)
            //holder.textinfo2.setVisibility(View.GONE)
            //holder.editview.setVisibility(View.VISIBLE)
        }
        return v
    }

    override fun getItem(position: Int): Any? {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun updateItemStr(position: Int, str: String?) {
        list.get(position).strinfo2 = str
    }

}

class CreateRoomListViewHolder(var viewItem: View) {
    var imageView1: ImageView = viewItem.findViewById<ImageView>(R.id.imageView1)
    var textinfo: TextView = viewItem.findViewById<TextView>(R.id.textinfo)
    var textinfo2: TextView = viewItem.findViewById<TextView>(R.id.textinfo2)
    var editview: EditText = viewItem.findViewById<EditText>(R.id.editviewinfo)
    var imageView2: ImageView = viewItem.findViewById<ImageView>(R.id.imageView2)
}
