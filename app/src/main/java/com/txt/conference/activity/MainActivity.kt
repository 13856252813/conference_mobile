package com.txt.conference.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.txt.conference.R
import com.txt.conference.adapter.ConferenceAdapter
import com.txt.conference.adapter.RecyclerViewDivider
import com.txt.conference.view.IConferenceView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), IConferenceView {

    var mConferenceAdapter: ConferenceAdapter? = null

    companion object {
        val KEY_USER = "key_user"
    }

    override fun addConferences(conference: List<String>) {

    }

    override fun jumpActivity() {

    }


    override fun back() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var data = ArrayList<String>()
        for (i in 0..99) {
            data.add("Jane $i")
        }

        mConferenceAdapter = ConferenceAdapter(R.layout.item_conference, data)
        var layoutManager = LinearLayoutManager(this)
        home_rv.adapter = mConferenceAdapter
        home_rv.layoutManager = layoutManager
        home_rv.addItemDecoration(RecyclerViewDivider(this))
    }




}
