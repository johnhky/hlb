package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.ConsultBean;
import com.hlb.haolaoban.databinding.MsgNotificationBinding;
import com.hlb.haolaoban.databinding.MsgReceiveImageBinding;
import com.hlb.haolaoban.databinding.MsgReceiveTextBinding;
import com.hlb.haolaoban.databinding.MsgSendAudioBinding;
import com.hlb.haolaoban.databinding.MsgSendImageBinding;
import com.hlb.haolaoban.databinding.MsgSendTextBinding;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.widget.RoundImageView;

import java.util.List;

/**
 * Created by heky on 2017/12/28.
 */

public class ConsultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<ConsultBean> list;

    public ConsultAdapter(Context context, List<ConsultBean> list) {
        this.context = context;
        this.list = list;
    }

    public void update(List<ConsultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        MsgNotificationBinding binding = DataBindingUtil.inflate(inflater, R.layout.msg_notification, parent, false);
        switch (viewType) {
            case 1:
                MsgSendTextBinding text = DataBindingUtil.inflate(inflater, R.layout.msg_send_text, parent, false);
                return new TextMyHolder(text.getRoot());
            case 2:
                MsgSendImageBinding image = DataBindingUtil.inflate(inflater, R.layout.msg_send_image, parent, false);
                return new ImageMyHolder(image.getRoot());
            case 3:
                MsgSendAudioBinding audio = DataBindingUtil.inflate(inflater, R.layout.msg_send_audio, parent, false);
                return new AudioMyHolder(audio.getRoot());
            case 4:
                MsgReceiveTextBinding yourText = DataBindingUtil.inflate(inflater, R.layout.msg_receive_text, parent, false);
                return new TextYourHolder(yourText.getRoot());
            case 5:
                MsgReceiveImageBinding yourImage = DataBindingUtil.inflate(inflater, R.layout.msg_receive_image, parent, false);
                return new ImageYourHolder(yourImage.getRoot());
        }
        return new TimeHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TextMyHolder) {

        } else if (holder instanceof ImageMyHolder) {

        } else if (holder instanceof AudioMyHolder) {

        } else if (holder instanceof TextYourHolder) {

        } else if (holder instanceof ImageYourHolder) {

        } else if (holder instanceof TimeHolder) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = list.get(position).getData().getType();
        if (list.get(position).getData().getOwner() == Settings.getUserProfile().getMid()) {
            switch (type) {
                case 1: {
                    return 1;
                }
                case 2: {
                    return 2;
                }
                case 3: {
                    return 3;
                }
            }
        } else {
            switch (type) {
                case 1: {
                    return 4;
                }
                case 2: {
                    return 5;
                }
                case 3: {
                    return 6;
                }
            }
        }
        return 96;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TextMyHolder extends RecyclerView.ViewHolder {

        RoundImageView avatar;
        TextView tv_msg;

        public TextMyHolder(View view) {
            super(view);
            avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        }
    }

    class ImageMyHolder extends RecyclerView.ViewHolder {

        RoundImageView avatar;
        ImageView iv_image;

        public ImageMyHolder(View view) {
            super(view);
            avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
        }
    }


    class AudioMyHolder extends RecyclerView.ViewHolder {

        RoundImageView avatar;
        TextView message_item_audio_duration;
        FrameLayout message_item_audio_container;

        public AudioMyHolder(View view) {
            super(view);
            avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            message_item_audio_duration = (TextView) view.findViewById(R.id.message_item_audio_duration);
            message_item_audio_container = (FrameLayout) view.findViewById(R.id.message_item_audio_container);
        }
    }

    class TextYourHolder extends RecyclerView.ViewHolder {

        RoundImageView avatar;
        TextView tv_name, tv_msg;

        public TextYourHolder(View view) {
            super(view);
            avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        }
    }

    class ImageYourHolder extends RecyclerView.ViewHolder {

        RoundImageView avatar;
        TextView tv_name;
        ImageView iv;

        public ImageYourHolder(View view) {
            super(view);
            avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            iv = (ImageView) view.findViewById(R.id.iv);
        }
    }

    class TimeHolder extends RecyclerView.ViewHolder {

        TextView tv_time;

        public TimeHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }

}
