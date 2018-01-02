package com.hlb.haolaoban.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.activity.mine.BigImageActivity;
import com.hlb.haolaoban.bean.ConsultBean;
import com.hlb.haolaoban.databinding.MsgNotificationBinding;
import com.hlb.haolaoban.databinding.MsgReceiveImageBinding;
import com.hlb.haolaoban.databinding.MsgReceiveTextBinding;
import com.hlb.haolaoban.databinding.MsgSendAudioBinding;
import com.hlb.haolaoban.databinding.MsgSendImageBinding;
import com.hlb.haolaoban.databinding.MsgSendTextBinding;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;
import com.hlb.haolaoban.widget.RoundImageView;

import java.io.IOException;
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
            case 95:
                return new TimeStartHolder(binding.getRoot());
            case 98:
                return new TimeEndHolder(binding.getRoot());
        }
        return new TimeHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TextMyHolder) {
            ((TextMyHolder) holder).tv_msg.setText(list.get(position).getData().getMsg());
            Glide.with(context).load(list.get(position).getData().getFrom().getHeadlogo()).fitCenter().into(((TextMyHolder) holder).avatar);
        } else if (holder instanceof ImageMyHolder) {
            Glide.with(context).load(list.get(position).getData().getMsg()).fitCenter().into(((ImageMyHolder) holder).iv_image);
            Glide.with(context).load(list.get(position).getData().getFrom().getHeadlogo()).fitCenter().into(((ImageMyHolder) holder).avatar);
            ((ImageMyHolder) holder).iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = BigImageActivity.intentFor(context, list.get(position).getData().getMsg());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        } else if (holder instanceof AudioMyHolder) {
            Glide.with(context).load(list.get(position).getData().getFrom().getHeadlogo()).fitCenter().into(((AudioMyHolder) holder).avatar);
            ((AudioMyHolder) holder).message_item_audio_duration.setText(list.get(position).getData().getLongs() + "\"");
            ((AudioMyHolder) holder).message_item_audio_container.setOnClickListener(new AudioClickListener(position, (AudioMyHolder) holder));
        } else if (holder instanceof TextYourHolder) {
            Glide.with(context).load(list.get(position).getData().getFrom().getHeadlogo()).fitCenter().into(((TextYourHolder) holder).avatar);
            ((TextYourHolder) holder).tv_name.setText(list.get(position).getData().getFrom().getName());
            ((TextYourHolder) holder).tv_msg.setText(list.get(position).getData().getMsg());
        } else if (holder instanceof ImageYourHolder) {
            Glide.with(context).load(list.get(position).getData().getFrom().getHeadlogo()).fitCenter().into(((ImageYourHolder) holder).avatar);
            ((ImageYourHolder) holder).tv_name.setText(list.get(position).getData().getFrom().getName());
            Glide.with(context).load(list.get(position).getData().getMsg()).fitCenter().into(((ImageYourHolder) holder).iv);
            ((ImageYourHolder) holder).iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = BigImageActivity.intentFor(context, list.get(position).getData().getMsg());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        } else if (holder instanceof TimeStartHolder) {
            String time = Utils.stampToHourMitues(list.get(position).getData().getTime() + "000");
            ((TimeStartHolder) holder).tv_time.setText("咨询开始:" + time);
        } else if (holder instanceof TimeEndHolder) {
            String time = Utils.stampToHourMitues(list.get(position).getData().getTime() + "000");
            ((TimeEndHolder) holder).tv_time.setText("咨询结束:" + time);
        } else if (holder instanceof TimeHolder) {
            ((TimeHolder) holder).tv_time.setText("现在暂时无人值班,请稍候");
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = list.get(position).getData().getType();
        switch (list.get(position).getMode()){
            case "consult":
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
                switch (type) {
                    case 95:
                        return 95;
                    case 98:
                        return 98;
                }
                break;
            case "order":

                break;
        }
        return 100;
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
        ImageView message_item_audio_playing_animation;

        public AudioMyHolder(View view) {
            super(view);
            avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
            message_item_audio_duration = (TextView) view.findViewById(R.id.message_item_audio_duration);
            message_item_audio_container = (FrameLayout) view.findViewById(R.id.message_item_audio_container);
            message_item_audio_playing_animation = (ImageView) view.findViewById(R.id.message_item_audio_playing_animation);
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

    class TimeStartHolder extends RecyclerView.ViewHolder {

        TextView tv_time;

        public TimeStartHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }

    class TimeEndHolder extends RecyclerView.ViewHolder {

        TextView tv_time;

        public TimeEndHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }


    class TimeHolder extends RecyclerView.ViewHolder {

        TextView tv_time;

        public TimeHolder(View view) {
            super(view);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
        }
    }

    class AudioClickListener implements View.OnClickListener {

        int position;
        AudioMyHolder holder;
        private MediaPlayer mediaPlayer;

        public AudioClickListener(int position, AudioMyHolder holder) {
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            holder.message_item_audio_playing_animation.setBackgroundResource(R.drawable.msg_audio_animation_right);
            final AnimationDrawable drawable = (AnimationDrawable) holder.message_item_audio_playing_animation.getBackground();
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            }
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                mediaPlayer = new MediaPlayer();
            }
            try {
                mediaPlayer.setDataSource(list.get(position).getData().getMsg());
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        drawable.start();
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();// 释放音频资源
                    Message message = new Message();
                    message.obj = holder.message_item_audio_playing_animation;
                    handler.sendMessage(message);
                }
            });

        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ImageView iv = (ImageView) msg.obj;
                ((AnimationDrawable) iv.getBackground()).stop();
                iv.clearAnimation();
                iv.setBackgroundResource(R.drawable.nim_audio_animation_list_right_3);
            }
        };
    }
}


