package com.txt.conference.event;

/**
 * Created by pc on 2017/12/11.
 */

public class MessageEvent {

    private String mMsg;
    public MessageEvent(String msg) {
        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }
}
