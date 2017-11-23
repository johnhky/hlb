package com.hlb.haolaoban.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;

import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.NotificationUtil;

/**
 * Created by heky on 2017/11/10.
 */

public class TotalRemindActivity extends BaseActivity {
    MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = new MediaPlayer();
        parseData();
    }

    private void parseData() {
        if (!TextUtils.isEmpty(getData())) {
            NotificationUtil.showNotification(mActivity,"", getData());
            DialogUtils.showRemindMsg(mActivity, getData(), new DialogUtils.OnDialogItemClickListener() {
                @Override
                public void onItemClick(int which) {
                    switch (which) {
                        case 1:
                            startActivity(PrescriptionActivity.class);
                            finish();
                            break;
                    }
                }
            });
        }
    }


    private String getData() {
        return getIntent().getStringExtra(Constants.DATA);
    }

}
