package com.mrg.mynotbook;



import android.app.SearchableInfo;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.PopupWindow;
import android.widget.RadioGroup;

import android.widget.TextView;

import com.mrg.mynotbook.adapter.DialogDiaryAdapter;
import com.mrg.mynotbook.adapter.MainListAdapter;

import com.mrg.mynotbook.adapter.QuiteAdapter;
import com.mrg.mynotbook.app.OpenAlbumActivity;
import com.mrg.mynotbook.config.MyNoteBookConfig;
import com.mrg.mynotbook.model.entities.ContentList;
import com.mrg.mynotbook.model.entities.Quite;

import com.mrg.mynotbook.presenter.impi.ImpiMainPresenter;
import com.mrg.mynotbook.ui.DiaryActivity;
import com.mrg.mynotbook.ui.NoteActivity;
import com.mrg.mynotbook.ui.MainView;
import com.mrg.mynotbook.ui.PhoneActivity;

import com.mrg.mynotbook.ui.ScrollingActivity;
import com.mrg.mynotbook.ui.SearchActivity;
import com.mrg.mynotbook.utils.AudioViewUtils;
import com.mrg.mynotbook.utils.StringUtils;
import com.mrg.mynotbook.widget.MultiDirectionSlidingDrawer;

import com.mrg.mynotbook.widget.MyDialogYseNo;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import com.xiaomi.ad.AdListener;
import com.xiaomi.ad.NativeAdInfoIndex;
import com.xiaomi.ad.NativeAdListener;
import com.xiaomi.ad.adView.StandardNewsFeedAd;
import com.xiaomi.ad.common.pojo.AdError;
import com.xiaomi.ad.common.pojo.AdEvent;

import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import tyrantgit.widget.HeartLayout;

public class MainActivity extends OpenAlbumActivity implements MainView, MainListAdapter.OnMainItemClickListener, Toolbar.OnMenuItemClickListener, DialogDiaryAdapter.OnRecyclerViewItemClickListener, AudioViewUtils.AudioListener, View.OnClickListener, QuiteAdapter.QuiteAdapterListener {



    public static final String TAG = "mrg";
    private BoomMenuButton menuButton;
    private ImpiMainPresenter impiMainPresenter;
    private final static int ADDDIARY = 0;
    private final static int ADDNOTE = 1;
    private final static int ADDPHONE = 2;

    private final static int QUITE_TEXT = 3;
    private final static int QUITE_IMAGE = 4;
    private final static int QUITE_AUDIO = 5;
    private RecyclerView rv_content;
    private MainListAdapter adapter;
    private boolean isExit = false;

    private HeartLayout heartLayout;
    private String coverIndex = "0";
    private View dialogView = null;
    private DialogDiaryAdapter DialogDiaryadpter;

    private RecyclerView rv_quite;
    private QuiteAdapter quiteAdapter;
    private View audioView;
    private Button audioButton;
    private AudioViewUtils audioViewUtils;
    private View quiteTextView;
    private EditText ed_quiteText;
    private PopupWindow audioPopuWindow;
    private PopupWindow quiteTextPopuWindow;
    private int OPEN_QUITE_TYPE;
    private Quite nowQuite;
    private MultiDirectionSlidingDrawer dr_quite;
    private TextView tv_null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("主页");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(this);

        impiMainPresenter = new ImpiMainPresenter(this, this);
        //初始化控件
        initView();
        impiMainPresenter.initview();



        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mrg.myNoteBook.UI.ADD_MAIN_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.ADD_QUITE_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.UP_MAIN_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.REMOVE_MAIN_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.DELETE_QUITE_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.UP_QUITE_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.DATA.UP_DATABASE");
        intentFilter.addAction("com.mrg.myNoteBook.UI.UP_DATA_ALL");

        registerReceiver(impiMainPresenter, intentFilter);
    }


    private void initView() {


        menuButton = (BoomMenuButton) findViewById(R.id.bmb);
        menuButton.setNormalColor(Color.parseColor("#4bd4c2"));
        for (int i = 0; i < menuButton.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            switch (index) {
                                case ADDDIARY:
                                    impiMainPresenter.addDiary();
                                    break;
                                case ADDNOTE:
                                    impiMainPresenter.addNote();
                                    break;
                                case ADDPHONE:
                                    impiMainPresenter.addPhone();

                                    break;
                                case QUITE_TEXT:
                                    impiMainPresenter.showQuiteText();
                                    break;
                                case QUITE_IMAGE:
                                    impiMainPresenter.showChooseImage();
                                    break;
                                case QUITE_AUDIO:
                                    impiMainPresenter.showQuiteAudio();
                                    break;


                            }
                        }
                    })
                    .normalImageRes(getImageDrawable(i))

                    .normalColorRes(getButtonColor(i))
                    .normalText(getButtonText(i));

            menuButton.addBuilder(builder);

        }
        tv_null = (TextView) findViewById(R.id.tv_null);
        rv_content = (RecyclerView) findViewById(R.id.rv_main_content);
        rv_quite = (RecyclerView) findViewById(R.id.rv_main_quite);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        rv_quite.setLayoutManager(layoutManager);
        rv_content.setLayoutManager(new GridLayoutManager(this, 3));
//        rv_content.setItemAnimator((new SlideInOutLeftItemAnimator(rv_content)));
//        rv_quite.setItemAnimator((new SlideInOutTopItemAnimator(rv_quite)));
        heartLayout = (HeartLayout) findViewById(R.id.heart_layout);

        audioView = View.inflate(MainActivity.this, R.layout.popuwindow_quite_audio, null);
        audioButton = (Button) audioView.findViewById(R.id.btn_audio_recoder);
        audioViewUtils = new AudioViewUtils(this, audioButton, rv_quite);
        audioViewUtils.setAudioListener(this);

        quiteTextView = View.inflate(MainActivity.this, R.layout.popuwindow_quite_text, null);
        Button btn_quiteText = (Button) quiteTextView.findViewById(R.id.btn_quite_text_ok);
        ed_quiteText = (EditText) quiteTextView.findViewById(R.id.ed_quite_text);
        btn_quiteText.setOnClickListener(this);

        dr_quite = (MultiDirectionSlidingDrawer) findViewById(R.id.dr_main_quite);
        dr_quite.setOnDrawerOpenListener(new MultiDirectionSlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                Log.i(TAG, "onDrawerOpened: 打开监听");
            }
        });
        dr_quite.setOnDrawerCloseListener(new MultiDirectionSlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                Log.i(TAG, "onDrawerClosed: 关闭监听");
            }
        });


    }


    private String[] buttonString = {"新建日记本", "新建便签", "新建电话本", "快速添加文字", "快速添加图片", "快速添加录音"};


    public String getButtonText(int index) {
        return buttonString[index];
    }


    private int[] imageDrawable = {R.drawable.diary_1, R.drawable.note, R.drawable.phone, R.drawable.quite_text, R.drawable.quite_image, R.drawable.quite_audio};


    public int getImageDrawable(int index) {
        return imageDrawable[index];
    }

    private int[] colors = {R.color.addDiary, R.color.addNote, R.color.addPhone, R.color.quiteTextColor, R.color.quiteImageColor, R.color.quiteAudioColor};

    private int getButtonColor(int index) {
        return colors[index];
    }

    @Override
    public void showList(List<ContentList> list, List<Quite> quiteList) {
        adapter = new MainListAdapter(this, list);
        quiteAdapter = new QuiteAdapter(this, quiteList);
        quiteAdapter.onQuiteAdapterListener(this);
        rv_content.setAdapter(adapter);
        rv_quite.setAdapter(quiteAdapter);

        adapter.onItemClickListenter(this);

    }

    private void gotoActivity(String intent, String id, String position, String who, Class activity) {
        Intent mintent = new Intent(MainActivity.this, activity);

        mintent.putExtra("Type", intent);
        mintent.putExtra("Id", id);
        mintent.putExtra("Position", position);
        mintent.putExtra("Who", who);
        startActivity(mintent);
    }

    @Override
    public void startNoteActivity(String intent, String id, String position) {
        gotoActivity(intent, id, position, "", NoteActivity.class);

    }

    @Override
    public void startPhoneActivity(String intent, String id, String position, String who) {
        gotoActivity(intent, id, position, who, PhoneActivity.class);

    }

    @Override
    public void startDiaryActivity(String intent, String id, String position, String who) {
        gotoActivity(intent, id, position, who, DiaryActivity.class);
//        Intent intent1 = new Intent(this, ScrollingActivity.class);
//        startActivity(intent1);
    }

    @Override
    public void updateListView(int position, ContentList contentList) {
        adapter.update(position, contentList);

    }

    @Override
    public void addDataListView(ContentList contentList) {
        if (dr_quite.isOpened()) {
            dr_quite.animateClose();
        }
        adapter.addDate(contentList);

    }

    @Override
    public void removeDataListView(int position) {
        adapter.removeDate(position);

    }

    /**
     * 获得一个自定义Dialog对象
     *
     * @return
     */
    private MyDialogYseNo.Builder getDialog() {
        MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
        return builder;
    }

    /**
     * 获得对应的View
     *
     * @param type
     * @return
     */
    private View getDialogView(int type) {

        switch (type) {
            case ADDDIARY:
                dialogView = View.inflate(this, R.layout.dialog_add_diary, null);
                break;

            case ADDPHONE:
                dialogView = View.inflate(this, R.layout.dialog_add_phone, null);
                break;
        }

        return dialogView;

    }

    /**
     * 获得日记dialog对象
     *
     * @return
     */
    private MyDialogYseNo.Builder getDiaryDialog() {
        coverIndex = "0"; //每次获取时初始化皮肤选择标识
        getDialogView(ADDDIARY);
        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.rv_dialog_diary_cover);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DialogDiaryadpter = new DialogDiaryAdapter(this);
        recyclerView.setAdapter(DialogDiaryadpter);
        DialogDiaryadpter.setOnItemClickListener(this);
        MyDialogYseNo.Builder builder = getDialog();
        builder.setContentView(dialogView);
        return builder;

    }

    /**
     * 获得一个电话簿dialog对象
     *
     * @return
     */
    private MyDialogYseNo.Builder getPhoneDialog() {
        getDialogView(ADDPHONE);
        MyDialogYseNo.Builder builder = getDialog();
        builder.setContentView(dialogView);

        return builder;
    }


    @Override
    public void addDiaryItem() {
        MyDialogYseNo.Builder builder = getDiaryDialog();
        builder.setTitle("新建日记本")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText username = (EditText) dialogView.findViewById(R.id.ed_dialog_diary_username);
                        String name = username.getText().toString();
                        if (name.equals("")) {

                            impiMainPresenter.saveDiary("我的日记", coverIndex);

                        } else {
                            impiMainPresenter.saveDiary(name, coverIndex);
                        }
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        DialogDiaryadpter.setCover(Integer.parseInt(coverIndex));


        builder.create().show();
    }

    @Override
    public void addPhoneItem() {

        MyDialogYseNo.Builder builder = getPhoneDialog();

        builder.setTitle("新建电话簿")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText username = (EditText) dialogView.findViewById(R.id.ed_dialog_phone_username);
                        String name = username.getText().toString();
                        if (name.equals("")) {

                            impiMainPresenter.savePhone("新电话簿");

                        } else {
                            impiMainPresenter.savePhone(name);
                        }
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    public void setDiaryItem(final ContentList contentList) {
        MyDialogYseNo.Builder builder = getDiaryDialog();
        final EditText diaryname = (EditText) dialogView.findViewById(R.id.ed_dialog_diary_username);
        diaryname.setText(contentList.getNumber());
        //光标移至末尾
        diaryname.setSelection(contentList.getNumber().length());
        if (contentList.getCover() != null) {
            coverIndex = contentList.getCover();
        } else {
            coverIndex = "0";

        }

        DialogDiaryadpter.setCover(Integer.parseInt(coverIndex));
        builder.setTitle("修改日记本")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = diaryname.getText().toString();

                        if (s.equals("")) {
                            contentList.setNumber("我的日记");
                        } else {
                            contentList.setNumber(s);
                        }
                        contentList.setCover(coverIndex);
                        impiMainPresenter.updataDiary(contentList);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    public void setPhoneItem(final ContentList contentList) {
        MyDialogYseNo.Builder builder = getPhoneDialog();
        final EditText diaryname = (EditText) dialogView.findViewById(R.id.ed_dialog_phone_username);
        diaryname.setText(contentList.getNumber());
        diaryname.setSelection(contentList.getNumber().length());
        builder.setTitle("修改电话簿")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String s = diaryname.getText().toString();
                        if (s.equals("")) {
                            contentList.setNumber("我的电话簿");
                        } else {
                            contentList.setNumber(s);
                        }
                        impiMainPresenter.updataPhone(contentList);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    public void showSureDelete(final ContentList contentList) {
        MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
        builder.setTitle("警告")
                .setContentView(View.inflate(MainActivity.this, R.layout.dialog_phone_layout, null))
                .setMessage("确定要删除这项及其里面的内容？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        impiMainPresenter.deleteItem(contentList);

                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    public void showIsNullText(boolean bool) {
        if (bool) {
            tv_null.setVisibility(View.VISIBLE);
        } else {
            tv_null.setVisibility(View.GONE);
        }
    }

    @Override
    public void showQuiteText() {
        if (!dr_quite.isOpened()) {
            dr_quite.animateOpen();
        }
        quiteTextPopuWindow = new PopupWindow(quiteTextView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        quiteTextPopuWindow.setBackgroundDrawable(new BitmapDrawable());
        quiteTextPopuWindow.setOutsideTouchable(true);
        quiteTextPopuWindow.setFocusable(true);
        quiteTextPopuWindow.update();
        quiteTextPopuWindow.showAtLocation(rv_quite, Gravity.BOTTOM, 0, 0);

        OPEN_QUITE_TYPE = 0;
    }

    @Override
    public void showQuiteImage() {
        if (!dr_quite.isOpened()) {
            dr_quite.animateOpen();
        }
        showChooseGetPhoto();
    }

    @Override
    public void showQuiteAudio() {
        if (!dr_quite.isOpened()) {
            dr_quite.animateOpen();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查录音权限
            if (checkRecordAudioPermissions() && checkWritePermission()) {
                showAudioUtils();
            } else {
                //弹窗警告窗
                showNotPermissionInfo(502, "需要权限", "需要获取录音和写入文件权限");
            }
        } else {
            showAudioUtils();
        }


    }

    private void showAudioUtils() {
        audioPopuWindow = new PopupWindow(audioView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        audioPopuWindow.setBackgroundDrawable(new BitmapDrawable());
        audioPopuWindow.setOutsideTouchable(true);
        audioPopuWindow.setFocusable(true);
        audioPopuWindow.update();
        audioPopuWindow.showAtLocation(rv_quite, Gravity.BOTTOM, 0, 0);


    }

    @Override
    public void getPermission(int code) {
        super.getPermission(code);
        //再次获取权限
        if (code == 502) {
            requestRecordAudioPermissions();
        }
        if (code==499){
            //获取必要权限
            requestNecessary();
        }

    }

    @Override
    public void addQuiteDateList(Quite newQuite) {
        quiteAdapter.add(newQuite);
        rv_quite.scrollToPosition(quiteAdapter.getItemCount() - 1);
        if (!dr_quite.isOpened()) {
            dr_quite.animateOpen();
        }

    }

    @Override
    public void deleteQuiteItem(int position) {
        quiteAdapter.removeData(position);
    }

    @Override
    public void updateQuiteItem(int position, Quite quite) {
        quiteAdapter.upDate(position, quite);

    }

    @Override
    public void openQuiteTextItem(Quite quite) {
        //获取内容
        String number = quite.getNumber();
        //显示内容
        ed_quiteText.setText(number);
        //光标移动到末尾
        ed_quiteText.setSelection(number.length());
        //显示快速添加文字窗口
        showQuiteText();
        //设置标识符为修改
        OPEN_QUITE_TYPE = 1;
        nowQuite = new Quite();
        nowQuite = quite;

    }

    @Override
    public void getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取必要权限
            if (checkWritePermission()  && checkReadPhoneStartPermission())
                ;
            else
                showNotPermissionInfo(499,"必要权限","获取的权限为必要权限\n没有该权限APP可能无法正常工作");
        }

    }

    @Override

    protected void onDestroy() {
        super.onDestroy();
        //注销广播
        unregisterReceiver(impiMainPresenter);
    }


    @Override
    public void onMainItemClickListener(ContentList contentList) {
        impiMainPresenter.goItem(contentList);
    }

    @Override
    public void deleteItemClickListener(ContentList contentList) {
        impiMainPresenter.sureDelete(contentList);
    }

    @Override
    public void editItemClickListener(ContentList contentList) {
        impiMainPresenter.editItem(contentList);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //退出键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Timer tExit = null;
            if (dr_quite.isOpened()) {
                dr_quite.animateClose();
            } else if (isExit == false) {

                isExit = true; // 准备退出
                showInfo("再按一次退出程序");
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false; // 取消退出
                    }
                }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

            } else {
                finish();
            }
        }


        return true;
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Random random = new Random();
                int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
                heartLayout.addHeart(ranColor);
                break;
            case R.id.search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;


        }

        return true;
    }

    @Override
    public void onItemClick(int position, View view) {
        coverIndex = position + "";
        DialogDiaryadpter.setCover(position);

    }

    @Override
    public void startRecordAudio() {
        impiMainPresenter.startAudio();
    }


    @Override
    public void saveRecordAudio() {
        impiMainPresenter.saveAudio();
    }

    @Override
    public void cancelRecordAudio() {
        impiMainPresenter.cancelAudio();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_quite_text_ok://快速添加文字确定按钮
                String number = ed_quiteText.getText().toString();
                if (!number.equals("")) {

                    if (OPEN_QUITE_TYPE == 0) {
                        //新建
                        impiMainPresenter.saveQuiteText(number);

                    } else if (OPEN_QUITE_TYPE == 1) {
                        //修改
                        nowQuite.setNumber(number);
                        impiMainPresenter.updateQuiteText(nowQuite);
                    }
                    if (quiteTextPopuWindow != null && quiteTextPopuWindow.isShowing()) {
                        quiteTextPopuWindow.dismiss();
                    }
                }

                break;
        }
    }

    @Override
    public void atPhotoGetImage(String fileName) {

        impiMainPresenter.saveQuiteImage(MyNoteBookConfig.imageUrl + fileName + ".jpg");
    }

    @Override
    public void atAlbumGetImage(String imgUri) {

        impiMainPresenter.saveQuiteImage(imgUri);
    }

    @Override
    public void deleteItem(String id, String position) {
        impiMainPresenter.deleteQuite(id, position);
    }

    @Override
    public void openItem(Quite quite) {
        switch (quite.getType()) {
            case "T":
                impiMainPresenter.goQuiteText(quite);
                break;
            case "N":
                impiMainPresenter.goNoteItem(quite.getId(), quite.getPosition());
                break;
            case "I":
                Intent intent = new Intent();
                intent.setAction(android.content.Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setDataAndType(Uri.parse(quite.getNumber()), "image/*");
                startActivity(intent);
                break;
            default:

                break;

        }

    }



}
