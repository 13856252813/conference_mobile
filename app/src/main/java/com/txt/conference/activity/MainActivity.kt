package com.txt.conference.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.common.utlis.ULog
import com.common.widget.LoadingView
import com.txt.conference.R
import com.txt.conference.adapter.ConferenceAdapter
import com.txt.conference.adapter.RecyclerViewDivider
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.GetRoomsPresenter
import com.txt.conference.presenter.JoinRoomPresenter
import com.txt.conference.presenter.LogoffPresenter
import com.txt.conference.view.IGetRoomsView
import com.txt.conference.view.IJoinRoomView
import com.txt.conference.view.ILogoffView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.layout_menu.*

class MainActivity : BaseActivity(), IGetRoomsView, IJoinRoomView, ILogoffView {
    val TAG = MainActivity::class.java.simpleName
    var getRoomsPresenter: GetRoomsPresenter? = null
    var mConferenceAdapter: ConferenceAdapter? = null
    var joinRoomPresenter: JoinRoomPresenter? = null
    var logoffPresenter: LogoffPresenter? = null

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
                    if (room.status == RoomBean.STATUS_BEGING) {
                        joinRoomPresenter?.joinRoom(room, getToken())
                    }
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

    private fun initView() {
        home_ll_create.setOnClickListener {
            var i = Intent(this, CreateConferenceRoomActivity::class.java)
            startActivity(i)
        }
        initRecyclerView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        initView()
        initInfomation()
        getRoomsPresenter = GetRoomsPresenter(this)
        joinRoomPresenter = JoinRoomPresenter(this)
        logoffPresenter = LogoffPresenter(this)

        home_ib_logoff.setOnClickListener { logoffPresenter?.logoff(getToken()) }
        home_iv_menu.setOnClickListener {
            if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
                drawer_layout.closeDrawer(Gravity.LEFT)
            } else {
                drawer_layout.openDrawer(Gravity.LEFT)
            }
        }
    }

    private fun initInfomation() {
        home_tv_name.setText(TxSharedPreferencesFactory(applicationContext).getUserName())
        home_tv_phone.setText(TxSharedPreferencesFactory(applicationContext).getPhoneNumber())
        home_tv_version.setText("版本：" + packageManager.getPackageInfo(packageName, 0).versionName)
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
