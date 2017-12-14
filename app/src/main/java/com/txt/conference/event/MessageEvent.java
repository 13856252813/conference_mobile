package com.txt.conference.event;

import com.google.gson.Gson;

/**
 * Created by pc on 2017/12/11.
 */

public class MessageEvent {

    private String eventCode;
    private String room;


    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String  room) {
        this.room = room;
    }

    public <T> T getDataObject(Class<T> classOfT) {
        Gson g = new Gson();
        T obj = g.fromJson(getRoom(), classOfT);
        return obj;
    }
}
