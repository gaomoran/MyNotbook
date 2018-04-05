package com.mrg.mynotbook.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.mrg.mynotbook.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2017-08-30.
 */
public class RichEditorText extends NestedScrollView {
    private LinearLayout allLayout;
    private LayoutInflater inflater;
    private OnKeyListener keyListener;
    private OnClickListener closeImageListener;
    private OnClickListener imageListener;
    private OnFocusChangeListener focusListener;
    private View lastFocusView;
    private int editNormalPadding;
    private OnClickListener audioListener;
    private MediaPlayer player;
    public static final String mBitmapTag = "☆";
    public static final String mBitmapStopTag = "★☆";
    public static final String mAudioTag = "☆";
    public static final String mAudioStopTag = "✦☆";
    private List<String> mContentList;
    private String TAG="mrg";

    public RichEditorText(Context context) {
        this(context, (AttributeSet) null);
    }

    public RichEditorText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichEditorText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(1);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        allLayout.setPadding(50, 15, 50, 15);
        addView(allLayout, layoutParams);
        keyListener = new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == 0 && event.getKeyCode() == 67) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }

                return false;
            }
        };

        focusListener = new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    lastFocusView =  v;
                }

            }
        };
        imageListener=new OnClickListener(){

            @Override
            public void onClick(View v) {
                lastFocusView=v;
            }
        };
        closeImageListener = new OnClickListener() {
            public void onClick(View v) {
                CardView viewParent =(CardView) v.getParent().getParent();
                onImageCloseClick(viewParent);
            }
        };

        player = new MediaPlayer();
        audioListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                onAudioPlay(v);
                lastFocusView=v;
            }
        };
        android.widget.LinearLayout.LayoutParams firstEditParam = new android.widget.LinearLayout.LayoutParams(-1, -1);
        EditText firstEdit = createEditText("记录你的一天吧！", dip2px(context, 5.0F));
        allLayout.addView(firstEdit, firstEditParam);
        lastFocusView = firstEdit;
    }

    /**
     * 退格删除
     *
     * @param editTxt
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1);
            if (null != preView) {
                if (preView instanceof CardView) {
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText) {
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();
                    allLayout.removeView(editTxt);
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    lastFocusView = preEdit;
                }else if (preView instanceof RelativeLayout){
                    allLayout.removeView(preView);
                }
            }
        }

    }

    /**
     * 删除图片
     *
     * @param view
     */
    private void onImageCloseClick(View view) {
        allLayout.removeView(view);
    }

    /**
     * 播放语音
     *
     * @param view
     */
    private void onAudioPlay(View view) {
        DataAudioView data =(DataAudioView) view.findViewById(R.id.rich_edit_audio_audioView);
        data.setFocusable(true);
        data.setFocusableInTouchMode(true);
        data.requestFocus();
        final ImageView bg =(ImageView) view.findViewById(R.id.img_rich_audio_play_background);
        String audioPath = data.getAudioPath();
        Log.i(TAG, "onAudioPlay: 进入播放");
        if (player.isPlaying()) {
            player.stop();
            Glide.with(RichEditorText.this.getContext()).load(R.drawable.audio_play).into(bg);
            player = new MediaPlayer();

        } else {
            Log.i(TAG, "onAudioPlay: 播放准备");
            try {
                player.setDataSource(audioPath);
                Log.i(TAG, "onAudioPlay: 数据载入");

            } catch (IOException e) {
                e.printStackTrace();
            }
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    player.start();
                    Glide.with(RichEditorText.this.getContext()).load(R.drawable.audio_play_now).into(bg);

                    Log.i(TAG, "onAudioPlay: 播放语音");
                }
            });
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                player = new MediaPlayer();
                Log.i(TAG, "onCompletion: 播放结束");
                Glide.with(RichEditorText.this.getContext()).load(R.drawable.audio_play).into(bg);

            }
        });
    }

    /**
     * 获取所有内容
     *
     * @return
     */
    public String getAllData() {
        String data = "";
        int num = allLayout.getChildCount();

        for (int index = 0; index < num; ++index) {
            View itemView = allLayout.getChildAt(index);
            if (itemView instanceof EditText) {
                EditText item = (EditText) itemView;
                data += item.getText().toString();
            } else if (itemView instanceof CardView) {
                DataImageView var7 = (DataImageView) itemView.findViewById(R.id.rich_edit_image_imageView);
                String path = var7.getAbsolutePath();
                data += mBitmapTag + path + mBitmapStopTag;
            } else if (itemView instanceof RelativeLayout) {
                DataAudioView audioView = (DataAudioView) itemView.findViewById(R.id.rich_edit_audio_audioView);
                String audioData = audioView.getAudioData();
                data += mAudioTag + audioData + mAudioStopTag;
            }
        }
        return data;
    }

    /**
     * 设置数据
     *
     * @param contentList
     */
    public void setmContentList(String contentList) {
        if (mContentList == null) {
            mContentList = new ArrayList<String>();
        }
        mContentList.clear();
        clearAllLayout();
        String content = contentList;
        if (content.length() > 0 && content.contains(mBitmapTag)) {
            String[] split = content.split("☆");
            for (String str : split) {
                mContentList.add(str);
            }
        } else {
            mContentList.add(content);
        }
        insertData();
    }

    /**
     * 设置数据
     */
    private void insertData() {
        if (mContentList.size() > 0) {
            for (String str : mContentList) {
                if (str.contains("★")) {//判断是否是图片地址
                    String path = str.replace("★", "");//还原地址字符串
                    addImageViewAtIndex(getLastIndex(), path);
                } else if (str.contains("✦")) {//判断是否为语音地址
                    String path1 = str.replace("✦", "");//还原地址字符串
                    addAudioViewAtIndex(getLastIndex(), path1);
                } else {
                    //插入文字
                    addEditTextAtIndex(getLastIndex(), str);
                }
            }
            //末尾加入输入框
            addEditTextAtIndex(getLastIndex(), "");
        }
    }

    /**
     * 初始化edittext
     *
     * @param hint
     * @param paddingTop
     * @return
     */
    public EditText createEditText(String hint, int paddingTop) {
        EditText editText = (EditText) inflater.inflate(R.layout.rich_edittext, (ViewGroup) null);
        editText.setOnKeyListener(keyListener);
        editText.setPadding(editNormalPadding, paddingTop, editNormalPadding, paddingTop);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        return editText;
    }

    /**
     * dip转px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public int dip2px(Context context, float dipValue) {
        float m = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * m + 0.5F);
    }

    /**
     * 清除全部布局
     */
    public void clearAllLayout() {
        allLayout.removeAllViews();
    }

    /**
     * 获取下一个角标
     *
     * @return
     */
    public int getLastIndex() {
        int lastEditIndex = allLayout.getChildCount();
        return lastEditIndex;
    }

    public void addEditTextAtIndex(int index, String editStr) {
        EditText editText2 = createEditText("", 10);
        editText2.setText(editStr);
        editText2.setOnFocusChangeListener(focusListener);
        allLayout.addView(editText2, index);
    }
    public void addImage(String imagePath){
        if (lastFocusView instanceof EditText){
            EditText editText = (EditText) lastFocusView;
            String number = editText.getText().toString();
            int index = editText.getSelectionStart();
            String substring = number.substring(index, number.length());
            int viewIndex = allLayout.indexOfChild(editText);
            editText.setText(number.substring(0,index));
            addImageViewAtIndex(viewIndex+1,imagePath);
            addEditTextAtIndex(viewIndex+2,substring);
        }else {
            addImageViewAtIndex(getLastIndex(),imagePath);
        }
    }


    public CardView addImageViewAtIndex(int index, String imagePath) {
        CardView imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout.findViewById(R.id.rich_edit_image_imageView);
        Glide.with(getContext()).load(imagePath).into(imageView);
        imageView.setAbsolutePath(imagePath);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        int imageHeight = allLayout.getWidth() * bmp.getHeight() / bmp.getWidth();
        android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(-1, 900);
        lp.bottomMargin = 10;
        imageView.setLayoutParams(lp);
        allLayout.addView(imageLayout, index);


        return  imageLayout;
    }

    public void addAudioViewAtIndex(int index, String audioPath) {
        RelativeLayout audioLayout = createAudioLayout();
        DataAudioView audioView = (DataAudioView) audioLayout.findViewById(R.id.rich_edit_audio_audioView);
        String[] split = audioPath.split("#");
        audioView.setAudioPath(split[0]);
        audioView.setAudioTime(split[1]);
        allLayout.addView(audioLayout, index);
        addEditTextAtIndex(getLastIndex(), "");
    }


    /**
     * 获得一个图片布局
     *
     * @return
     */
    private CardView createImageLayout() {
        CardView layout = (CardView) inflater.inflate(R.layout.rich_edit_image, (ViewGroup) null);
        View closeView = layout.findViewById(R.id.rich_edit_image_close);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(closeImageListener);
        return layout;
    }

    /**
     * 获得一个录音布局
     *
     * @return
     */
    private RelativeLayout createAudioLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.rich_edit_audio, (ViewGroup) null);
        View view = layout.findViewById(R.id.rich_edit_audio_view);
        view.setOnClickListener(audioListener);
        return layout;
    }

}
