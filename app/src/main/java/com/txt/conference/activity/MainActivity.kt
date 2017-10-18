package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.adapter.ConferenceAdapter
import com.txt.conference.adapter.RecyclerViewDivider
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.GetRoomsPresenter
import com.txt.conference.presenter.JoinRoomPresenter
import com.txt.conference.view.IGetRoomsView
import com.txt.conference.view.IJoinRoomView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), IGetRoomsView, IJoinRoomView {
    val TAG = MainActivity::class.java.simpleName
    var getRoomsPresenter: GetRoomsPresenter? = null
    var mConferenceAdapter: ConferenceAdapter? = null
    var joinRoomPresenter: JoinRoomPresenter? = null

    override fun jumpToRoom(room: RoomBean, connect_token: String) {
        var i = Intent(this, RoomActivity::class.java)
        i.putExtra(RoomActivity.KEY_ROOM, room)
        i.putExtra(RoomActivity.KEY_CONNECT_TOKEN, connect_token)
        startActivity(i)
    }

    override fun showError(errorRes: Int) {
        Toast.makeText(this, errorRes, Toast.LENGTH_SHORT).show()
    }

    override fun jumpToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun addConferences(conference: List<RoomBean>?) {
        ULog.d(TAG, "addConferences")
        if (mConferenceAdapter == null) {
            mConferenceAdapter = ConferenceAdapter(R.layout.item_conference, conference)
            mConferenceAdapter?.onItemChildClickListener = object : BaseQuickAdapter.OnItemChildClickListener {
                override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                    var room = adapter?.data?.get(position) as RoomBean
                    ULog.d(TAG, "onItemChildClick $position roomId:" + room.roomId)
                    joinRoomPresenter?.joinRoom(room, getToken())
                }
            }
            home_rv.adapter = mConferenceAdapter
        } else {
            mConferenceAdapter?.setNewData(conference)
            mConferenceAdapter?.notifyDataSetChanged()
        }
    }

    companion object {
        val KEY_USER = "key_user"
    }

    override fun jumpActivity() {

    }


    override fun back() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        getRoomsPresenter = GetRoomsPresenter(this)
        joinRoomPresenter = JoinRoomPresenter(this)

//        ULog.d(TAG, "onTick time is " + Date().time)
//        var countDown = object : CountDownTimer((10 * 1000), 1000) {
//            override fun onFinish() {
//                ULog.d(TAG,"onFinish")
//            }
//
//            override fun onTick(p0: Long) {
//                ULog.d(TAG,"onTick $p0")
//            }
//        }
//        countDown.start()
//        ULog.d(TAG, "Hi :" + DateUtils().format(50 * 60 * 1000L - TimeZone.getDefault().rawOffset, DateUtils.HH_mm_ss))
    }

    override fun onResume() {
        super.onResume()
        getRoomsPresenter?.getRooms(getToken())
    }

    override fun onPause() {
        super.onPause()
        mConferenceAdapter?.cancelAllTimers()
    }

    fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(this)
        home_rv.layoutManager = layoutManager
        home_rv.addItemDecoration(RecyclerViewDivider(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        ULog.d(TAG, "onDestroy")
    }



}
