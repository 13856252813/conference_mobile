package com.txt.conference.model

import com.txt.conference.bean.RoomBean
import java.io.Serializable

/**
 * Created by pc on 2017/12/13.
 */

class MediaModel : Serializable{
    var code: Int = 0
    var data: RoomBean? = null
}
