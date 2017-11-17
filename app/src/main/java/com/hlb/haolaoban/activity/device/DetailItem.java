package com.hlb.haolaoban.activity.device;

import java.util.UUID;

/**
 * Created by heky on 2017/11/17.
 */

public class DetailItem {

    public static final int TYPE_SERVICE = 0;
    public static final int TYPE_CHARACTER = 1;

    public int type;

    public UUID uuid;

    public UUID service;

    public DetailItem(int type, UUID uuid, UUID service) {
        this.type = type;
        this.uuid = uuid;
        this.service = service;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getService() {
        return service;
    }

    public void setService(UUID service) {
        this.service = service;
    }
}



