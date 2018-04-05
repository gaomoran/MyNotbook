package com.mrg.mynotbook.presenter.impi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mrg.mynotbook.model.SearchModel;
import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.model.entities.PhoneUser;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.model.entities.SearchEntity;
import com.mrg.mynotbook.model.impi.ImpQuiteModel;
import com.mrg.mynotbook.model.impi.ImpiSearchModel;
import com.mrg.mynotbook.presenter.SearchPresenter;
import com.mrg.mynotbook.ui.SearchView;

import java.util.List;

/**
 * Created by MrG on 2018-03-18.
 */

public class ImpiSearchPresenter extends BroadcastReceiver implements SearchPresenter {

    private SearchModel impiSearchModel;
    private SearchView searchView;
    private final ImpQuiteModel quiteModel;
    private Context context;
    public ImpiSearchPresenter(Context context, SearchView searchView) {
        this.searchView=searchView;
        this.context=context;
        impiSearchModel = new ImpiSearchModel(context);
        quiteModel = new ImpQuiteModel(context);
    }

    @Override
    public void Search(String keyword) {
        //清空上次查询的数据
        searchView.removeSearchData();
        //显示本次查询数据
        List<SearchEntity> searchEntities = impiSearchModel.SearchDiaryData(keyword);
        searchView.showSearchDiaryData(searchEntities);
        List<SearchEntity> quites = impiSearchModel.SearchQuiteData(keyword);
        searchView.showSearchQuiteData(quites);
        List<SearchEntity> phoneUsers = impiSearchModel.SearchPhoneData(keyword);
        searchView.showSearchPhoneData(phoneUsers);
    }

    @Override
    public void updateQuite(SearchEntity entity) {
        Quite quite = quiteModel.getQuite(entity.getId());
        quite.setNumber(entity.getSearchNumber());
        quiteModel.setQuite(quite);
        Intent intent = new Intent();
        intent.setAction("com.mrg.myNoteBook.UI.UP_DATA_ALL");
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case "com.mrg.myNoteBook.UI.UP_DATA_ALL":
            case "com.mrg.myNoteBook.UI.UP_DIARY_DATA":
                searchView.reloadSearchData();
                break;

        }
    }
}
