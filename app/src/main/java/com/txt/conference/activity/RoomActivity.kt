package com.txt.conference.activity

import android.os.Bundle
import com.txt.conference.R

/**
 * Created by jane on 2017/10/15.
 */
class RoomActivity : BaseActivity() {
    companion object {
        var KEY_ROOM = "room"
        var KEY_CONNECT_TOKEN = "connect_token"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
    }
}