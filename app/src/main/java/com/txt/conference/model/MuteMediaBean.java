package com.txt.conference.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by pc on 2017/12/26.
 */

public class MuteMediaBean implements Serializable {


    /**
     * muteDes : 1
     * muteType : audioMute
     * room : {"creator":{"uid":"account/abc","display":"abc"},"roomNo":"685285","topic":"无会议主题","start":1514276580000,"participants":[{"audioMute":1,"name":"方林","videoMute":0,"mobile":"13856252813","id":"account/fl","usertype":"account","email":"lin.fang@pivosgroup.com.cn"}],"delaytime":0,"duration":1,"roomId":"rooms/abc/1514276580000/1514276616009","id":"5a420710396fbb091c9c8348","createAt":1514276616009}
     * eventCode : muteUser
     */

    private int muteDes;
    private String muteType;
    private RoomBean room;
    private String eventCode;

    public int getMuteDes() {
        return muteDes;
    }

    public void setMuteDes(int muteDes) {
        this.muteDes = muteDes;
    }

    public String getMuteType() {
        return muteType;
    }

    public void setMuteType(String muteType) {
        this.muteType = muteType;
    }

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

    public static class RoomBean {
        /**
         * creator : {"uid":"account/abc","display":"abc"}
         * roomNo : 685285
         * topic : 无会议主题
         * start : 1514276580000
         * participants : [{"audioMute":1,"name":"方林","videoMute":0,"mobile":"13856252813","id":"account/fl","usertype":"account","email":"lin.fang@pivosgroup.com.cn"}]
         * delaytime : 0
         * duration : 1
         * roomId : rooms/abc/1514276580000/1514276616009
         * id : 5a420710396fbb091c9c8348
         * createAt : 1514276616009
         */

        private CreatorBean creator;
        private String roomNo;
        private String topic;
        private long start;
        private int delaytime;
        private int duration;
        private String roomId;
        private String id;
        private long createAt;
        private List<ParticipantsBean> participants;

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public String getRoomNo() {
            return roomNo;
        }

        public void setRoomNo(String roomNo) {
            this.roomNo = roomNo;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public int getDelaytime() {
            return delaytime;
        }

        public void setDelaytime(int delaytime) {
            this.delaytime = delaytime;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getCreateAt() {
            return createAt;
        }

        public void setCreateAt(long createAt) {
            this.createAt = createAt;
        }

        public List<ParticipantsBean> getParticipants() {
            return participants;
        }

        public void setParticipants(List<ParticipantsBean> participants) {
            this.participants = participants;
        }

        public static class CreatorBean {
            /**
             * uid : account/abc
             * display : abc
             */

            private String uid;
            private String display;

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getDisplay() {
                return display;
            }

            public void setDisplay(String display) {
                this.display = display;
            }
        }

        public static class ParticipantsBean {
            /**
             * audioMute : 1
             * name : 方林
             * videoMute : 0
             * mobile : 13856252813
             * id : account/fl
             * usertype : account
             * email : lin.fang@pivosgroup.com.cn
             */

            private int audioMute;
            private String name;
            private int videoMute;
            private String mobile;
            private String id;
            private String usertype;
            private String email;

            public int getAudioMute() {
                return audioMute;
            }

            public void setAudioMute(int audioMute) {
                this.audioMute = audioMute;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getVideoMute() {
                return videoMute;
            }

            public void setVideoMute(int videoMute) {
                this.videoMute = videoMute;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsertype() {
                return usertype;
            }

            public void setUsertype(String usertype) {
                this.usertype = usertype;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }
}
