package com.hlb.haolaoban.handler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.hlb.haolaoban.bean.DrugRemind;
import com.hlb.haolaoban.service.AlarmReceiver;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static android.content.Context.ALARM_SERVICE;

/**
 * Created by heky on 2017/11/20.
 */

public class MsgHandler {

    public static final long ONE_DAY = 1000 * 60 * 60 * 24;

    /*将websocket推送过来的信息保存到本地数据库*/
    public static void saveMsg(final List<DrugRemind> list, final String mid) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
                for (int i = 0; i < list.size(); i++) {
                    DrugRemind data = new DrugRemind();
                    data.setId(i);
                    data.setMid(mid);
                    data.setStat_his(list.get(i).getStat_his());
                    data.setMsg(list.get(i).getMsg());
                    data.setEndtime(list.get(i).getEndtime());
                    realm.copyToRealm(data);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
            }
        });
    }

    /*查询所有的用药提醒并且升序排序*/
    public static RealmResults<DrugRemind> queryAllMsg(String mid) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DrugRemind> list = realm.where(DrugRemind.class).equalTo("mid", mid).findAllSorted("stat_his", Sort.ASCENDING);
        return list;
    }

    /*将同一时间段的用药信息放在一起*/
    public static String queryDrugMsg(String endtime) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DrugRemind> list = realm.where(DrugRemind.class).equalTo("stat_his", endtime).findAll();
        Map<String, DrugRemind> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            map.put(list.get(i).getStat_his(), list.get(i));
        }
        List<DrugRemind> datas = new ArrayList<>();
        Collection<DrugRemind> items = map.values();
        Iterator<DrugRemind> data = items.iterator();
        while (data.hasNext()) {
            datas.add(data.next());
        }
        String result = "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i).getMsg() + ",");
        }
        if (!TextUtils.isEmpty(builder.deleteCharAt(builder.length() - 1))) {
            result = builder.deleteCharAt(builder.length() - 1).toString();
        }
        return result;
    }

    /*查询今天所有时间段的用药提醒并且设置闹钟提醒*/
    public static void queryMsg(String mid, Context context) {
        final RealmResults<DrugRemind> list = queryAllMsg(mid);
        long currentTime = System.currentTimeMillis();
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                String doTime = Utils.stampToDate(currentTime + "") + " " + list.get(i).getStat_his() + ":00";
                String time = list.get(i).getEndtime() + "000";
                long endTime = Long.parseLong(time);
                long doTimeStamp = Utils.timeToStamp(doTime);
                if (doTimeStamp > currentTime && currentTime < endTime) {
                    String msg = queryDrugMsg(list.get(i).getStat_his());
                    Intent to = new Intent(context, AlarmReceiver.class);
                    to.putExtra(Constants.DATA, msg);
                    to.setAction(Constants.CHANNEL);
                    PendingIntent sender = PendingIntent.getBroadcast(context, 0, to, FLAG_UPDATE_CURRENT);
                    AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    if (currentTime + ONE_DAY <= endTime) {
                        manager.setRepeating(AlarmManager.RTC_WAKEUP, doTimeStamp, ONE_DAY, sender);
                    } else {
                        manager.set(AlarmManager.RTC_WAKEUP, doTimeStamp, sender);
                    }
                    break;
                }

            }
        }
    }

}
