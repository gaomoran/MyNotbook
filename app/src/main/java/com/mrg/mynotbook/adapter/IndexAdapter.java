package com.mrg.mynotbook.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mrg.mynotbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2018-01-01.
 */
public class IndexAdapter extends PagerAdapter {
    private List<View> list;
    private int curUpdatePager;
    private String TAG="mrg";
    private IndexAdapterListener indexAdapterListener;
    public IndexAdapter(Context context){
        LayoutInflater from = LayoutInflater.from(context);
        list=new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View inflate = from.inflate(R.layout.item_ad_index, null);
            list.add(inflate);
        }


    }
    public void updateView(View view, int index){
        curUpdatePager = index;
        list.remove(index);
        list.add(index, view);
        indexAdapterListener.isLoadAd();
        notifyDataSetChanged();
    }

    interface IndexAdapterListener{

        void isLoadAd();
    }
    public void setIndexAdapterListener(IndexAdapterListener indexAdapterListener){
        this.indexAdapterListener=indexAdapterListener;

    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        list.get(position).setTag(position);
        container.addView(list.get(position));
        return list.get(position);

    }
    @Override
    public int getItemPosition(Object object) {
        View view = (View)object;
        if(curUpdatePager == (Integer)view.getTag()){
            Log.i(TAG, "getItemPosition: 重建子项目");

            return POSITION_NONE;
        }else{
            return POSITION_UNCHANGED;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(list.get(position));
    }
}
