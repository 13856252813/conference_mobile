package com.txt.conference.model

import com.common.bean.UpLoadInfo
import com.common.http.HttpEventHandler
import com.common.http.HttpUploadsFactoryBase
import com.common.utlis.ULog
import com.txt.conference.application.TxApplication
import com.txt.conference.bean.GetFaceAuthBean
import com.txt.conference.bean.GetLoginBean
import com.txt.conference.bean.LoginBean
import com.txt.conference.data.TxSharedPreferencesFactory
import com.txt.conference.http.FaceAuthHttpFactory
import com.txt.conference.http.FaceLoginHttpFactory
import com.txt.conference.http.LoginHttpFactory
import java.util.ArrayList


/**
 * Created by pc on 2017/12/1.
 */
class FaceAuthModel : IFaceAuthModel {
    override var status: Int = Status.FAILED
    override var msg: String? = null

    var mPreference: TxSharedPreferencesFactory? = null
    var mFaceAuthHttp: FaceAuthHttpFactory? = null


    override fun authcheck(token: String, strpath: String, loginCallBack: IBaseModel.IModelCallBack) {
        if (mFaceAuthHttp == null) {
            mFaceAuthHttp = FaceAuthHttpFactory()
            mFaceAuthHttp?.setHttpEventHandler(object : HttpEventHandler<GetFaceAuthBean>() {
                override fun HttpFailHandler() {
                    status = Status.FAILED_UNKNOW
                    loginCallBack.onStatus()
                }

                override fun HttpSucessHandler(result: GetFaceAuthBean?) {
                    status = result?.code!!
                    if (status == Status.SUCCESS) {

                    } else {
                        msg = result?.msg
                    }
                    loginCallBack.onStatus()
                }
            })
        }

        var uploadlist = ArrayList<UpLoadInfo>()
        var uploadinfoFile = UpLoadInfo()        // File
        uploadinfoFile.keyname = "faceimages"
        uploadinfoFile.uploadPathOrString = strpath
        uploadinfoFile.uploadtype = 1
        uploadinfoFile.mineType = "image/jpeg"
        uploadlist.add(uploadinfoFile)
        mFaceAuthHttp?.uploadInfos = uploadlist
        mFaceAuthHttp?.DownloaDatas(token)

    }
}