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
import android.text.Editable
import android.support.v7.widget.RecyclerView.ViewHolder
import android.text.TextWatcher
import com.common.utlis.ULog


/**
 * Created by pc on 2017/10/15.
 */


class CreateRoomListAdapter(val list: ArrayList<CreateRoomListAdapterBean>, val context: Context) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }
    var clickFirst = false
    var editText = ""
    val TAG = CreateRoomListAdapter::class.java.simpleName
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
        //ULog.i("test", "getview position:" + position)
        holder.textinfo.text = list[position].strinfo
        holder.textinfo2.text = list[position].strinfo2
        holder.imageView1.setImageResource(list[position].icon)
        holder.imageView2.setImageResource(list[position].icon2)

        class MyTextWatcher(private val mHolder: CreateRoomListViewHolder) : TextWatcher {

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null){
                    editText = s.toString()
                }
            }
        }
        if (holder.textinfo.text.equals("会议主题") && !clickFirst){
            holder.imageView2.visibility = View.GONE
            holder.textinfo2.visibility = View.VISIBLE
            holder.editview.visibility = View.GONE
            if (!editText.equals("")){
                holder.textinfo2.text = editText
            }
            //ULog.i(TAG, "position:" + position)
            //ULog.i(TAG, "holder.textinfo.text:" + holder.textinfo.text)
            //holder.textinfo2.setVisibility(View.GONE)
            //holder.editview.setVisibility(View.VISIBLE)
        } else if (holder.textinfo.text.equals("会议主题") && clickFirst){

            holder.textinfo2.setVisibility(View.GONE)
            holder.editview.setVisibility(View.VISIBLE)
        }

        holder.editview.addTextChangedListener(MyTextWatcher(holder))

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

    fun updateItemStr(click: Boolean) {
        clickFirst = click
    }
}

class CreateRoomListViewHolder(var viewItem: View) {
    var imageView1: ImageView = viewItem.findViewById<ImageView>(R.id.imageView1)
    var textinfo: TextView = viewItem.findViewById<TextView>(R.id.textinfo)
    var textinfo2: TextView = viewItem.findViewById<TextView>(R.id.textinfo2)
    var editview: EditText = viewItem.findViewById<EditText>(R.id.editviewinfo)
    var imageView2: ImageView = viewItem.findViewById<ImageView>(R.id.imageView2)
}
