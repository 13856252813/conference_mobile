package com.txt.conference.event;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pc on 2017/12/11.
 */

public class MessageEvent {

    public static final String MUTETOUSERS="muteToUsers";
    public static final String EVENTCODE="eventCode";


    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     *解析json数据
     */
    public String getEventCode(){
        if(getContent()!=null && !getContent().equals("")){
            try {
                JSONObject object=new JSONObject(getContent());
                String code=object.optString(EVENTCODE);
                return code;
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /**
     * 获取整个对象
     * @param classOfT
     * @param <T>
     * @return
     */
    public <T> T getDataObject(Class<T> classOfT) {
        if(getContent()!=null && !getContent().equals("")){
            try {
                JSONObject object=new JSONObject(getContent());
                Gson g = new Gson();
                T obj = g.fromJson(object.toString(), classOfT);
                return obj;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public <T> T getRoomBean(Class<T> classOfT){
        if(getContent()!=null && !getContent().equals("")){
            try {
                JSONObject object=new JSONObject(getContent());
                JSONObject roomObj=object.optJSONObject("room");
                Gson g = new Gson();
                T obj = g.fromJson(roomObj.toString(), classOfT);
                return obj;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }



}
