package com.hlb.haolaoban.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hlb.haolaoban.base.BaseActivity;
import com.hlb.haolaoban.R;
import com.hlb.haolaoban.bean.ImageBean;
import com.hlb.haolaoban.databinding.ActivityFeedbackBinding;
import com.hlb.haolaoban.http.Api;
import com.hlb.haolaoban.http.SimpleCallback;
import com.hlb.haolaoban.module.ApiModule;
import com.hlb.haolaoban.module.HttpUrls;
import com.hlb.haolaoban.utils.Constants;
import com.hlb.haolaoban.utils.DialogUtils;
import com.hlb.haolaoban.utils.Settings;
import com.hlb.haolaoban.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;


/**
 * Created by heky on 2017/11/6.
 */

public class FeedBackActivity extends BaseActivity {

    ActivityFeedbackBinding binding;
    private List<Bitmap> imgBase64;
    private final int CAMERA_REQUEST = 2;
    private final int PHOTO_REQUEST = 3;
    private float dp;
    private MyAdapter mAdapter;
    private List<String> pictureList;
    ApiModule api = Api.of(ApiModule.class);
    Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);
        initView();
    }

    protected void initView() {
        binding.titlebar.tbTitle.setText("意见反馈");
        imgBase64 = new ArrayList<>();
        pictureList = new ArrayList<>();
        dp = getResources().getDimension(R.dimen.dp_16);
        binding.titlebar.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFeedback();
            }
        });
        initBitmap();
    }

    private void uploadFeedback() {
        if (TextUtils.isEmpty(binding.etKeyword.getText().toString().trim()) || binding.etKeyword.getText().toString().trim().length() < 1) {
            showToast("意见反馈不能为空!");
            return;
        }
        Map<String, String> params = new LinkedHashMap<>();
        params.putAll(Constants.addParams());
        params.put("param[mid]", Settings.getUserProfile().getMid() + "");
        params.put("param[conetnt]", binding.etKeyword.getText().toString().trim());
        if (!pictureList.isEmpty()) {
            params.put("param[img]", pictureList.toString());
        }
        params.put("method", "public.feedback.add");
        api.getBaseUrl(params).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                showToast("多谢您的建议,您的建议是我们不断前进的动力!");
                finish();
            }
        });
    }

    private void initBitmap() {
        binding.tvPiece.setText("(" + imgBase64.size() + "/3)");
        mAdapter = new MyAdapter();
        int size = 0;
        if (imgBase64.size() < 3) {
            size = imgBase64.size() + 1;
        } else {
            size = imgBase64.size();
        }
        ViewGroup.LayoutParams params = binding.gridView.getLayoutParams();
        final int width = size * (int) (dp * 9.4f);
        params.width = width;
        binding.gridView.setLayoutParams(params);
        binding.gridView.setColumnWidth((int) (dp * 6.4f));
        binding.gridView.setStretchMode(GridView.NO_STRETCH);
        binding.gridView.setNumColumns(size);
        binding.gridView.setAdapter(mAdapter);
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogUtils.showPickPhotoDialog(view, FeedBackActivity.this, new DialogUtils.OnDialogItemClickListener() {
                    @Override
                    public void onItemClick(int which) {
                        switch (which) {
                            case 1:
                                takePicture();
                                break;
                            case 2:
                                choosePicture();
                                break;
                        }
                    }
                });
            }
        });
    }

    public void takePicture() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, CAMERA_REQUEST);
    }

    /**
     * 从相册获取
     */
    public void choosePicture() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    if (data.getExtras() != null) {
                        if (null != data.getExtras().get("data")) {
                            Bitmap bm = (Bitmap) data.getExtras().get("data");
                            imgBase64.add(bm);
                            uploadPicture(bm);
                            initBitmap();
                        }
                    }
                    break;
                case PHOTO_REQUEST:
                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        if (null != uri) {
                            try {
                                Bitmap bm = Utils.getBitmapFormUri(FeedBackActivity.this, uri);
                                imgBase64.add(bm);
                                uploadPicture(bm);
                                initBitmap();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void uploadPicture(Bitmap bm) {
        DialogUtils.showLoading(mActivity, "正在上传中...");
        String base64 = Utils.bitmapToString(bm);
        api.uploadImage(HttpUrls.uploadImage(base64)).enqueue(new SimpleCallback() {
            @Override
            protected void handleResponse(String response) {
                showToast("上传成功!");
                DialogUtils.hideLoading(mActivity);
                ImageBean data = gson.fromJson(response, ImageBean.class);
                pictureList.add(data.getImage_patg());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                DialogUtils.hideLoading(mActivity);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (imgBase64.size() < 3) {
                return imgBase64.size() + 1;
            }
            return imgBase64.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        ViewHolder holder;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(FeedBackActivity.this).inflate(R.layout.item_images, null);
                holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == imgBase64.size()) {
                holder.iv_image.setBackgroundResource(R.drawable.images_add);
                holder.iv_delete.setVisibility(View.GONE);
                if (position == 3) {
                    holder.iv_image.setVisibility(View.GONE);
                }
            } else {
                holder.iv_image.setImageBitmap(imgBase64.get(position));
                holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imgBase64.remove(position).recycle();
                        initBitmap();
                    }
                });
            }
            return convertView;
        }

        class ViewHolder {
            ImageView iv_image, iv_delete;
        }
    }


}
