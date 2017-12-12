package com.txt.conference.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.KeyEvent
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.adapter.ConferenceAdapter
import com.txt.conference.adapter.RecyclerViewDivider
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.presenter.*
import com.txt.conference.utils.CustomAttendDialog
import com.txt.conference.view.*
import com.txt.conference.widget.CustomDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.item_conference_new.*
import kotlinx.android.synthetic.main.layout_menu.*
import java.util.*
import android.support.v4.widget.DrawerLayout
import android.util.Log
import android.view.View
import com.common.utlis.DateUtils
import com.txt.conference.event.MessageEvent
import com.txt.conference.http.Urls
import com.txt.conference.utils.CommonUtils
import com.txt.conference.utils.StatusBarUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


class MainActivity : BaseActivity(), IGetRoomsView, IGetRoomInfoView, IJoinRoomView, IDeleteRoomView, ILogoffView, IInviteUsersView, ConferenceAdapter.TimeCallBack {

    override fun getRoomInfoFinished(getRoom: Boolean?, roombean: RoomBean?) {
        if (getRoom == true){
            joinRoomPresenter?.joinRoom(roombean!!, getToken())
        } else {
            getRoomsPresenter?.getRooms(getToken())
        }
    }

    override fun setAttendeeNumber(number: Int) {
        ULog.d(TAG, "setAttendeeNumber $number")
    }

    override fun inviteComplete(room: RoomBean) {
        showToast(R.string.tips_invite_success)
    }

    override fun getRoomId(): String? {
        if (selectroom == null){
            return null
        } else {
            return selectroom?.roomId
        }
    }



    val TAG = MainActivity::class.java.simpleName
    var getRoomsPresenter: GetRoomsPresenter? = null
    var mConferenceAdapter: ConferenceAdapter? = null
    var joinRoomPresenter: JoinRoomPresenter? = null
    var logoffPresenter: LogoffPresenter? = null
    var deleteRoomPresenter: DeleteRoomPresenter? = null

    var getRoomInfoPresenter: GetRoomInfoPresenter? = null

    var selectroom: RoomBean? = null

    var mCurrentTime:Long = 0

    lateinit var inviteUsersPresenter: InviteUsersPresenter

    fun getUserId(): String? {
        return TxSharedPreferencesFactory(applicationContext).getId()
    }
    override fun jumpToRoom(room: RoomBean, connect_token: String) {
        var i = Intent(this, RoomActivity::class.java)
        i.putExtra(RoomActivity.KEY_ROOM, room)
        i.putExtra(RoomActivity.KEY_CONNECT_TOKEN, connect_token)
        startActivity(i)
    }

    override fun deleteFinished() {
        getRoomsPresenter?.getRooms(getToken())
    }

    override fun showError(errorRes: Int) {
        Toast.makeText(this, errorRes, Toast.LENGTH_SHORT).show()
    }

    override fun jumpToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        this.finish()
    }

    override fun onFinish() {
        getRoomsPresenter?.getRooms(getToken())
    }

    override fun getToken(): String? {
        return TxSharedPreferencesFactory(applicationContext).getToken()
    }

    override fun addConferences(conference: List<RoomBean>?) {
        ULog.d(TAG, "addConferences")
        mConferenceAdapter?.setNewData(conference)
        mConferenceAdapter?.notifyDataSetChanged()
    }

    companion object {
        val KEY_USER = "key_user"
        val REQUEST_ATTEND = 101
        val REQUEST_PHONE = 102
    }

    override fun jumpActivity() {

    }


    override fun back() {

    }

    private fun initView() {
        home_ll_create.setOnClickListener {
            var i = Intent(this, CreateConferenceRoomActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left)
        }
        initRecyclerView()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main2)
        super.onCreate(savedInstanceState)

        initView()
        initInfomation()
        getRoomsPresenter = GetRoomsPresenter(this)
        joinRoomPresenter = JoinRoomPresenter(this)
        logoffPresenter = LogoffPresenter(this)
        deleteRoomPresenter = DeleteRoomPresenter(this)
        inviteUsersPresenter = InviteUsersPresenter(this)
        getRoomInfoPresenter = GetRoomInfoPresenter(this)
        home_ib_logoff.setOnClickListener {
            CustomDialog.showSelectDialog(this,resources.getString(R.string.logoff_sure),
                    object :com.txt.conference.widget.CustomDialog.DialogClickListener{
                        override fun confirm() {
                            logoffPresenter?.logoff(getToken())
                        }
                        override fun cancel() {
                        }
                    })
        }
        home_iv_menu.setOnClickListener {
            if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
                drawer_layout.closeDrawer(Gravity.LEFT)
            } else {
                drawer_layout.openDrawer(Gravity.LEFT)
            }
        }

        menu_facesetting_face_layout.setOnClickListener {
            startFaceLoginSettings()
        }

        menu_about_layout.setOnClickListener {
            startAbout()
        }
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onEventMainThread(event: MessageEvent) {
        Log.e("fl","--content:"+event.msg)
    }


    fun startFaceLoginSettings() {
        var i = Intent(this, FaceLoginSettingsActivity::class.java)
        startActivity(i)
    }

    fun startAbout() {
        var i = Intent(this, AboutActivity::class.java)
        startActivity(i)
    }

    private fun initInfomation() {
        home_tv_name.text = TxSharedPreferencesFactory(applicationContext).getUserName()
        home_tv_phone.text = TxSharedPreferencesFactory(applicationContext).getPhoneNumber()
        home_tv_version.text = "版本：" + packageManager.getPackageInfo(packageName, 0).versionName
    }

    override fun onResume() {
        super.onResume()
        getRoomsPresenter?.getRooms(getToken())

    }

    override fun onPause() {
        super.onPause()
        mConferenceAdapter?.cancelAllTimers()
    }

    private fun OpenPhoneAddress(room: RoomBean){
        ULog.i(TAG, "OpenPhoneAddress" )
        /*var smsToUri = Uri.parse("smsto:")
        var intent = Intent(Intent.ACTION_SENDTO, smsToUri)
        var date=DateUtils()
        var str_sms_Message = String.format(getString(R.string.sms_message), room?.creator?.display,
                date.format(room?.start, DateUtils.yyyy_MM_dd__HH_mm_ss),room?.roomNo)
        intent.putExtra("sms_body", getString(R.string.sms_message))
        startActivity(intent)*/

        /*var date= DateUtils()
        ULog.i(TAG, "startSendSms" )
        var smsToUri = Uri.parse("smsto:")
        var intent = Intent(Intent.ACTION_SENDTO, smsToUri)
        var str_sms_Message = String.format(getString(R.string.sms_message), room?.creator?.display,
                date.format(room?.start,DateUtils.HH_mm), room?.roomNo, Urls.HOST)
        intent.putExtra("sms_body", str_sms_Message)
        startActivity(intent)*/

        CommonUtils.startSendSms(this, room!!)
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>?) {
        //clientPresenter.joinRoom(getConnectToken())
        //val uri = Uri.parse("content://contacts/people")
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, REQUEST_PHONE)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>?) {
        Toast.makeText(this, "拒绝获取手机通讯录权限", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        ULog.i(TAG, "request:" + requestCode)
        ULog.i(TAG, "result:" + resultCode)

        if (REQUEST_ATTEND == requestCode ) {
            if (data != null) {
                var namelist: ArrayList<String>? = null
                var displaylist: ArrayList<String>? = null
                namelist  = data?.getStringArrayListExtra("nameattandList")
                displaylist = data?.getStringArrayListExtra("displayattandList")
                if (namelist != null){
                    for (i in namelist.indices){
                        val inviteBean: AttendeeBean = AttendeeBean()
                        inviteBean!!.display = displaylist[i]
                        inviteBean!!.uid = namelist[i]
                        ULog.i(TAG, "namelist:" + inviteBean!!.uid)
                        ULog.i(TAG, "displaylist:" + inviteBean!!.display)
                        inviteBean!!.invited = true
                        inviteUsersPresenter.changeInviteList(inviteBean)
                    }
                }
                inviteUsersPresenter.invite(getRoomId(), getToken())

            }
            return
        }
        if (requestCode == REQUEST_PHONE) {
            if (data != null) {
                val uri: Uri = data.getData()
                val contacts = getPhoneContacts(uri, this)
                ULog.i(TAG, contacts?.get(0))
                ULog.i(TAG, contacts?.get(1))

                val inviteBean: AttendeeBean = AttendeeBean()
                inviteBean!!.display = contacts?.get(0)
                inviteBean!!.uid = contacts?.get(1)
                inviteBean!!.invited = true
                ULog.i(TAG, "inviteBean:")
                ULog.i(TAG, contacts?.get(1))
                inviteUsersPresenter.changeInviteList(inviteBean)
                inviteUsersPresenter.invite(getRoomId(), getToken())
                /*val cursorLoader = CursorLoader(this, uri, null, null, null, null);
                val cursor = cursorLoader.loadInBackground()
                if (cursor != null) {
                    cursor.moveToFirst()
                    val contactId = cursor?.getString(cursor
                            .getColumnIndex(ContactsContract.Contacts._ID))
                    ULog.i(TAG, contactId)
                    val cr = this.contentResolver
                    val phone = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                    + contactId, null, null)

                    while (phone.moveToNext()) {
                        val usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        ULog.i(TAG, usernumber)
                    }
                }*/
            }
        }


    }

    private fun getPhoneContacts(uri: Uri, context: Context): Array<String?>? {
        val contact = arrayOfNulls<String>(2)
        //得到ContentResolver对象
        val cr = context.contentResolver
        //取得电话本中开始一项的光标
        val cursor = cr.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            //取得联系人姓名
            val nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)

            contact[0] = cursor.getString(nameFieldColumnIndex)
            ULog.i(TAG, "Name:" + contact[0])
            //取得电话号码
            val ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
            ULog.i(TAG, "ContactId:" + ContactId)
            val phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null)
            if (phone != null) {
                ULog.i(TAG, "phone uri:" + phone.toString())
                phone.moveToFirst()
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                /*while (phone.moveToNext()) {
                    ULog.i(TAG, phone.getInt(0).toString())
                    ULog.i(TAG, phone.getString(1))
                    ULog.i(TAG, phone.getString(2))
                }*/
            } else {
                ULog.i(TAG, "phone: null" )
            }
            if (phone != null){
                phone!!.close()
            }

            cursor.close()
        } else {
            return null
        }
        return contact
    }

    fun startCommpanAttendActivity(room: RoomBean){
        var i = Intent(this, ChooseManActivity::class.java)
        i.putExtra(ChooseManActivity.KEY_ROOM, room)
        //var requestCode: Int = 101
        startActivityForResult(i, REQUEST_ATTEND)
        //startActivity(i)
    }

    fun startDeviceActivity(room: RoomBean){
        var i = Intent(this, ChooseDeviceActivity::class.java)
        i.putExtra(ChooseDeviceActivity.KEY_ROOM, room)
        //var requestCode: Int = 101
        startActivityForResult(i, REQUEST_ATTEND)
        //startActivity(i)
    }


    fun showChoosAttendDialog(room: RoomBean){
        val builder = CustomAttendDialog.Builder(this)
        selectroom = room
        builder.setcompanyButton{
            dialog, _ -> dialog.dismiss()
            startCommpanAttendActivity(room)
        }
        builder.setdeviceButton{
            dialog, _ -> dialog.dismiss()
            startDeviceActivity(room)
        }
        builder.setphoneButton{
            dialog, _ -> dialog.dismiss()
            OpenPhoneAddress(room)
        }
        /*builder.setweixinButton(){
            dialog, which -> dialog.dismiss()
        }*/
        builder.setCancelButton(){
            dialog, which -> dialog.dismiss()
        }
        builder.create().show()
    }

    private fun showConfirmDialog(room: RoomBean){
        CustomDialog.showSelectDialog(this,resources.getString(R.string.delete_conference_confirm),
                object :com.txt.conference.widget.CustomDialog.DialogClickListener{
                    override fun confirm() {
                        deleteRoomPresenter?.deleteRoom(room, getToken())
                    }

                    override fun cancel() {
                    }

                })
    }
    private fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(this)
        home_rv.layoutManager = layoutManager
        home_rv.addItemDecoration(RecyclerViewDivider(this, 20, 20))
        mConferenceAdapter = ConferenceAdapter(R.layout.item_conference_new, null)
        ULog.d(TAG, "getUserId :" + getUserId())
        mConferenceAdapter?.setUid(this.getUserId())
        mConferenceAdapter?.timeCallBack = this
        mConferenceAdapter?.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            var room = adapter?.data?.get(position) as RoomBean
            ULog.d(TAG, "onItemChildClick $position ")
            when(view?.id) {
                item_bt_enter.id -> {
                    ULog.d(TAG, "onItemChildClick $position roomId:" + room.roomId)
                    if (room.status == RoomBean.STATUS_BEGING) {
                        //joinRoomPresenter?.joinRoom(room, getToken())
                        getRoomInfoPresenter?.getRoomInfo(room.roomNo, getToken())
                    }
                }
                add_attend.id -> {
                    ULog.d(TAG, "onItemChildClick $position add_attend")
                    showChoosAttendDialog(room)
                }
                delete_button.id -> {
                    ULog.d(TAG, "onItemChildClick $position delete")
                    //deleteRoomPresenter?.deleteRoom(room, getToken())
                    showConfirmDialog(room)
                }
            }
        }
        mConferenceAdapter?.bindToRecyclerView(home_rv)
        mConferenceAdapter?.setEmptyView(R.layout.layout_empty)

        layout_swipe_refresh.setOnRefreshListener {
            getRoomsPresenter?.getRooms(getToken())
            layout_swipe_refresh.setRefreshing(false)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        ULog.d(TAG, "onDestroy")
        EventBus.getDefault().unregister(this)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event!!.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - mCurrentTime > 2000) {
                showToast("再按一次返回键退出应用")
                mCurrentTime = System.currentTimeMillis()
                return true
            } else {
                finish()
                System.exit(0)
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun setStatusBar() {
        var mStatusBarColor = resources.getColor(R.color.colorPrimary)
        StatusBarUtil.setColorForDrawerLayout(this, findViewById<View>(R.id.drawer_layout) as DrawerLayout,
                mStatusBarColor, 112)
    }
}
