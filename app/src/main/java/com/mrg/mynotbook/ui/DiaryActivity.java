package com.mrg.mynotbook.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.mrg.mynotbook.R;
import com.mrg.mynotbook.adapter.DiaryListAdapter;
import com.mrg.mynotbook.app.BaseActivity;
import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.presenter.impi.ImpiDiaryPresenter;
import com.mrg.mynotbook.utils.SpaceItemDecoration;
import com.mrg.mynotbook.widget.MyDialogYseNo;

import java.util.List;

import it.gmariotti.recyclerview.itemanimator.SlideInOutLeftItemAnimator;
import it.gmariotti.recyclerview.itemanimator.SlideInOutTopItemAnimator;

public class DiaryActivity extends BaseActivity implements DiaryView, DiaryListAdapter.OnDiaryItemClickListente, Toolbar.OnMenuItemClickListener, View.OnClickListener {

    public static final String TAG = "mrg";
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private ImpiDiaryPresenter impiDiaryPresenter;
    private DiaryListAdapter adapter;
    private static String who="";

    private TextView tv_null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolBar(toolbar);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle("日记本");
        toolbar.inflateMenu(R.menu.menu_diary);

        tv_null = (TextView) findViewById(R.id.tv_null);
        recyclerView = (RecyclerView) findViewById(R.id.rv_diary_list);
        manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
//        recyclerView.addItemDecoration(new SpaceItemDecoration(0));


        who=getIntent().getStringExtra("Who");
        impiDiaryPresenter = new ImpiDiaryPresenter(this,this);

        impiDiaryPresenter.initView(who);

        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mrg.myNoteBook.UI.ADD_DIARY_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.UP_DIARY_DATA");
        intentFilter.addAction("com.mrg.myNoteBook.UI.REMOVE_DIARY_DATA");
        registerReceiver(impiDiaryPresenter, intentFilter);
    }

    @Override
    public void showList(List<Diary> list) {
        adapter = new DiaryListAdapter(this, list);
        adapter.onItemClickListente(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public Diary getNowDiary() {
        return null;
    }

    @Override
    public void startEditDiary(String id,String position) {
        Intent intent = new Intent(this,EditDiaryActivity.class);
        intent.putExtra("Type", "show");
        intent.putExtra("Id",id);
        intent.putExtra("Who",who);
        intent.putExtra("Position",position);
        startActivity(intent);

    }

    @Override
    public void addItem(Diary diary) {
//        Log.i(TAG, "addItem: 新增子项");
        adapter.addData(diary);
        manager.scrollToPositionWithOffset(0,0);

        tv_null.setVisibility(View.GONE);

    }

    @Override
    public void deleteItem(String id) {
        adapter.removeData(Integer.parseInt(id));
    }

    @Override
    public void updataItem(String id, Diary diary) {
        adapter.upData(Integer.parseInt(id), diary);
    }

    @Override
    public void showSureDelete(final String id, final String position) {
        MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
        builder.setTitle("警告")
                .setContentView(View.inflate(this,R.layout.dialog_phone_layout,null))
                .setMessage("确定要删除这篇日记吗？")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        impiDiaryPresenter.deleteDiary(id,position);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    @Override

    public void showNull(boolean b) {
        if (b) {
            tv_null.setVisibility(View.VISIBLE);
        } else {
            tv_null.setVisibility(View.GONE);
        }
    }

    @Override

    public void onDiaryItemClickListente(String id,String position) {
        impiDiaryPresenter.onItemClickListenter(id, position);
    }

    @Override
    public void deleteDiaryItemClickListente(String id, String position) {
        impiDiaryPresenter.sureDelete(id, position);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        Intent intent = new Intent(this, EditDiaryActivity.class);
        intent.putExtra("Type", "add");
        intent.putExtra("Id","new");
        intent.putExtra("Position", "-1");
        intent.putExtra("Who",who);
        startActivity(intent);

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(impiDiaryPresenter);
    }



}
