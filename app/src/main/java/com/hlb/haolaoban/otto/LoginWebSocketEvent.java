package com.hlb.haolaoban.otto;

/**
 * Created by heky on 2017/11/14.
 */

public class LoginWebSocketEvent extends Event {

    private String clubId;
    private String mId;

    public LoginWebSocketEvent(String clubId, String mId) {
        this.clubId = clubId;
        this.mId = mId;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
