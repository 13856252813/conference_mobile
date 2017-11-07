package com.txt.conference.activity

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.common.utlis.ULog
import com.txt.conference.R
import com.txt.conference.adapter.ConferenceAdapter
import com.txt.conference.adapter.RecyclerViewDivider
import com.txt.conference.bean.RoomBean
import com.txt.conference.data.TxSharedPreferencesFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.item_conference_new.*
import kotlinx.android.synthetic.main.layout_control.*
import kotlinx.android.synthetic.main.layout_menu.*
import com.txt.conference.utils.CustomDialog
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.txt.conference.bean.AttendeeBean
import com.txt.conference.presenter.*
import com.txt.conference.utils.CustomAttendDialog
import com.txt.conference.view.*
import java.util.ArrayList
import pub.devrel.easypermissions.EasyPermissions


class MainActivity : BaseActivity(), IGetRoomsView, IJoinRoomView, IDeleteRoomView, ILogoffView, IInviteUsersView, ConferenceAdapter.TimeCallBack {
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

    var selectroom: RoomBean? = null

    var mCurrentTime:Long = 0

    lateinit var inviteUsersPresenter: InviteUsersPresenter

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
        deleteRoomPresenter = DeleteRoomPresenter(this)
        inviteUsersPresenter = InviteUsersPresenter(this)
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

    fun OpenPhoneAddress(){
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) !== PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_CONTACTS),
                        0)
            }
        } else {
            val uri = Uri.parse("content://contacts/people")
            val intent = Intent(Intent.ACTION_PICK, uri)
            startActivityForResult(intent, REQUEST_PHONE)
        }*/
        ULog.i(TAG, "OpenPhoneAddress" )
        var args = arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_CONTACTS)
        if (EasyPermissions.hasPermissions(this, *args)) {
            ULog.i(TAG, "OpenPhoneAddress hasPermission" )
            val uri = Uri.parse("content://contacts/people")
            val intent = Intent(Intent.ACTION_PICK, uri)
            startActivityForResult(intent, REQUEST_PHONE)
        } else {
            ULog.i(TAG, "OpenPhoneAddress requestPermissions" )
            EasyPermissions.requestPermissions(this, getString(R.string.permission_phone_address), 100, *args)
        }
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

    fun showChoosAttendDialog(room: RoomBean){
        val builder = CustomAttendDialog.Builder(this)
        selectroom = room
        builder.setcompanyButton(){
            dialog, which -> dialog.dismiss()
            startCommpanAttendActivity(room)
        }
        builder.setdeviceButton(){
            dialog, which -> dialog.dismiss()
        }
        builder.setphoneButton(){
            dialog, which -> dialog.dismiss()
            OpenPhoneAddress()
        }
        builder.setweixinButton(){
            dialog, which -> dialog.dismiss()
        }
        builder.setCancelButton(){
            dialog, which -> dialog.dismiss()
        }
        builder.create().show()
    }

    fun showConfirmDialog(room: RoomBean){
        val builder = CustomDialog.Builder(this)
        builder.setMessage(getString(R.string.delete_conference_confirm))
        builder.setTitle(getString(R.string.delete_conference))
        builder.setPositiveButton(getString(R.string.confirm)) { dialog, which ->
            dialog.dismiss()
            deleteRoomPresenter?.deleteRoom(room, getToken())
        }

        builder.setNegativeButton(getString(R.string.cancel)
        ) { dialog, which -> dialog.dismiss() }

        builder.create().show()
    }
    fun initRecyclerView() {
        var layoutManager = LinearLayoutManager(this)
        home_rv.layoutManager = layoutManager
        home_rv.addItemDecoration(RecyclerViewDivider(this, 20, 20))
        mConferenceAdapter = ConferenceAdapter(R.layout.item_conference_new, null)
        mConferenceAdapter?.timeCallBack = this
        mConferenceAdapter?.onItemChildClickListener = object : BaseQuickAdapter.OnItemChildClickListener {
            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                var room = adapter?.data?.get(position) as RoomBean
                ULog.d(TAG, "onItemChildClick $position ")
                when(view?.id) {
                    item_bt_enter.id -> {
                        ULog.d(TAG, "onItemChildClick $position roomId:" + room.roomId)
                        if (room.status == RoomBean.STATUS_BEGING) {
                            joinRoomPresenter?.joinRoom(room, getToken())
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
        }
        mConferenceAdapter?.bindToRecyclerView(home_rv)
        mConferenceAdapter?.setEmptyView(R.layout.layout_empty)
    }

    override fun onDestroy() {
        super.onDestroy()
        ULog.d(TAG, "onDestroy")
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

}
