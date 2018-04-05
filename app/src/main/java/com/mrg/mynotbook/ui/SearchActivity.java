package com.mrg.mynotbook.ui;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import com.mrg.mynotbook.MainActivity;
import com.mrg.mynotbook.R;
import com.mrg.mynotbook.adapter.SearchAdapter;
import com.mrg.mynotbook.app.BaseActivity;
import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.model.entities.SearchEntity;
import com.mrg.mynotbook.presenter.SearchPresenter;
import com.mrg.mynotbook.presenter.impi.ImpiSearchPresenter;
import com.mrg.mynotbook.widget.MyDialogYseNo;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SearchView, SearchAdapter.SearchOnClickListener {

    private EditText ed_search;
    private RecyclerView rv_data;
    private ImpiSearchPresenter searchPresenter;
    private final static String TAG="mrg";
    private SearchAdapter searchAdapter;
    private View quiteTextView;
    private EditText ed_quiteText;
    private PopupWindow quiteTextPopuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchPresenter = new ImpiSearchPresenter(this,this);
        //初始化控件
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.mrg.myNoteBook.UI.UP_DATA_ALL");
        intentFilter.addAction("com.mrg.myNoteBook.UI.UP_DIARY_DATA");
        registerReceiver(searchPresenter,intentFilter);

    }
    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolBar(toolbar);
        ed_search = (EditText) findViewById(R.id.ed_search);
        rv_data = (RecyclerView) findViewById(R.id.rv_search_data);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(manager);
        searchAdapter = new SearchAdapter(this);
        searchAdapter.setItemOnOnClickListener(this);
        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = ed_search.getText().toString();
                if (keyword.length()>0) {
                    searchPresenter.Search(keyword);
                }
            }
        });
        quiteTextView = View.inflate(this, R.layout.popuwindow_quite_text, null);
        ed_quiteText = (EditText) quiteTextView.findViewById(R.id.ed_quite_text);

    }
    public void openQuiteTextItem(SearchEntity searchEntity) {
        //获取内容
        String number = searchEntity.getSearchNumber();
        //显示内容
        ed_quiteText.setText(number);
        //光标移动到末尾
        ed_quiteText.setSelection(number.length());
        //显示快速添加文字窗口
        showPopuWindow(searchEntity);
    }

    private void showPopuWindow(final SearchEntity searchEntity){
        quiteTextPopuWindow = new PopupWindow(quiteTextView, RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        quiteTextPopuWindow.setBackgroundDrawable(new BitmapDrawable());
        quiteTextPopuWindow.setOutsideTouchable(true);
        quiteTextPopuWindow.setFocusable(true);
        quiteTextPopuWindow.update();
        quiteTextPopuWindow.showAtLocation(rv_data, Gravity.BOTTOM, 0, 0);
        Button btn_enter = (Button) quiteTextView.findViewById(R.id.btn_quite_text_ok);
        btn_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ed_quiteText.getText().toString();
                searchEntity.setSearchNumber(number);
                searchPresenter.updateQuite(searchEntity);
                quiteTextPopuWindow.dismiss();
            }
        });
    }
    private void showSearchData(List<SearchEntity> data){
        searchAdapter.addData(data);
        rv_data.setAdapter(searchAdapter);
    }
    @Override
    public void showSearchDiaryData(List<SearchEntity> data) {

        showSearchData(data);
    }

    @Override
    public void showSearchQuiteData(List<SearchEntity> data) {
        showSearchData(data);
    }

    @Override
    public void showSearchPhoneData(List<SearchEntity> data) {
        showSearchData(data);
        List<SearchEntity> searchEntities = new ArrayList<>();
        searchEntities.add(new SearchEntity());
        showSearchData(searchEntities);
    }

    @Override
    public void removeSearchData() {
        searchAdapter.removeAll();
    }

    @Override
    public void reloadSearchData() {
        String keyword = ed_search.getText().toString();
        searchPresenter.Search(keyword);
    }

    @Override
    public void ItemOnClickListener(SearchEntity searchEntity) {
        switch (searchEntity.getSearchType()){
            case "Diary":
                Intent intent = new Intent(this,EditDiaryActivity.class);
                intent.putExtra("Type", "show");
                intent.putExtra("Id",searchEntity.getId());
                intent.putExtra("Who",searchEntity.getWho());
                intent.putExtra("Position","0");
                startActivity(intent);
                break;
            case "Quite":
                openQuiteTextItem(searchEntity);
                break;
            case "Phone":
                showPhoneDialog(searchEntity);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(searchPresenter);
    }
    private void showPhoneDialog(final SearchEntity search){
        MyDialogYseNo.Builder builder = new MyDialogYseNo.Builder(this);
        View view = View.inflate(this, R.layout.dialog_phone_layout, null);
        builder.setTitle("提醒")
                .setContentView(view)
                .setMessage("确定以下拨打电话?\n" + search.getSearchNumber())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        call(search.getSearchNumber());
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

    private void call(String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNumber);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            showInfo("没有权限");
            return;
        }
        startActivity(intent);
    }
}
