package com.txt.conference.model;

import com.txt.conference.bean.RoomBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2017/12/18.
 */

public class InviteEventBean implements Serializable {
    /**
     * room : {"creator":{"uid":"account/cde","display":"cde"},"roomNo":"319256","topic":"无会议主题","start":1513588440000,"participants":[{"audioMute":0,"name":"方林","videoMute":0,"mobile":"13856252813","id":"account/fl","usertype":"account","email":"lin.fang@pivosgroup.com.cn"}],"delaytime":0,"duration":1,"roomId":"rooms/cde/1513588440000/1513588449170","createAt":1513588449170}
     * eventCode : roomAttached
     */
    private RoomBean room;
    private String eventCode;

    public RoomBean getRoom() {
        return room;
    }

    public void setRoom(RoomBean room) {
        this.room = room;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }


}
