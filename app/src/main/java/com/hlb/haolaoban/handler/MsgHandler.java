package com.hlb.haolaoban.handler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.hlb.haolaoban.bean.DrugRemind;
import com.hlb.haolaoban.service.AlarmReceiver;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.Utils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by heky on 2017/11/20.
 */

public class MsgHandler {

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

    public static RealmResults<DrugRemind> queryAllMsg(String mid) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<DrugRemind> list = realm.where(DrugRemind.class).equalTo("mid", mid).findAllSorted("stat_his", Sort.ASCENDING);
        return list;
    }

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
                    Intent to = new Intent(context, AlarmReceiver.class);
                    to.putExtra(Constants.DATA, list.get(i).getMsg());
                    to.setAction(Constants.CHANNEL);
                    PendingIntent sender = PendingIntent.getBroadcast(context, 0, to, 0);
                    AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                    if (currentTime + 86400000 <= endTime) {
                        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, doTimeStamp, 1000 * 60 * 60 * 24, sender);
                    } else {
                        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, doTimeStamp, sender);
                    }
                    Log.e("eeee", doTimeStamp + " , " + doTime);
                }

            }
        }
    }

}
