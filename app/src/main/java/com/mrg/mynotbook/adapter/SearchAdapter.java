package com.mrg.mynotbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mrg.mynotbook.R;
import com.mrg.mynotbook.model.entities.Diary;
import com.mrg.mynotbook.model.entities.SearchEntity;
import com.mrg.mynotbook.utils.IndexFristName;
import com.xiaomi.ad.AdListener;
import com.xiaomi.ad.NativeAdInfoIndex;
import com.xiaomi.ad.NativeAdListener;
import com.xiaomi.ad.adView.StandardNewsFeedAd;
import com.xiaomi.ad.common.pojo.AdError;
import com.xiaomi.ad.common.pojo.AdEvent;

import java.util.ArrayList;
import java.util.List;

import static com.mrg.mynotbook.adapter.MainListAdapter.TAG;

/**
 * Created by MrG on 2018-03-20.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<SearchEntity> dataList;
    private Context context;
    private SearchOnClickListener searchOnClickListener;
    private final int TYPE_SEARCH = 456;
    private final int TYPE_SEARCH_ADVERT = 465;

    public SearchAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        dataList = new ArrayList<>();
    }

    public void addData(List<SearchEntity> list) {
        dataList.addAll(list);
        notifyItemRangeInserted(dataList.size() - list.size(), dataList.size());
    }

    public void removeAll() {
        dataList.clear();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < dataList.size()-1) {
            return TYPE_SEARCH;
        } else {
            return TYPE_SEARCH_ADVERT;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_SEARCH:
                View view = inflater.inflate(R.layout.item_search, parent, false);
                return new SearchHolder(view);
            default:
                View search_advert = inflater.inflate(R.layout.item_search_advert, parent, false);
                return new SearchAdvertHolder(search_advert);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchHolder) {
            switch (dataList.get(position).getSearchType()) {
                case "Diary":
                    ((SearchHolder)holder).title.setVisibility(View.VISIBLE);
                    ((SearchHolder)holder).title.setText(dataList.get(position).getTitle());
                    ((SearchHolder)holder).date.setText(dataList.get(position).getDate());
                    ((SearchHolder)holder).tv_form.setText("来自日记");
                    ((SearchHolder)holder).iv_type.setBackgroundResource(R.color.addDiary);
                    Glide.with(context).load(R.drawable.diary_1).into(((SearchHolder)holder).iv_type);
                    break;
                case "Quite":
                    ((SearchHolder)holder).title.setVisibility(View.GONE);
                    ((SearchHolder)holder).tv_form.setTextSize(13);
                    ((SearchHolder)holder).tv_form.setText("来自快速记录");
                    ((SearchHolder)holder).date.setText(dataList.get(position).getDate());
                    ((SearchHolder)holder).iv_type.setBackgroundResource(R.color.quiteTextColor);
                    Glide.with(context).load(R.drawable.quite).into(((SearchHolder)holder).iv_type);
                    break;
                case "Phone":
                    ((SearchHolder)holder).iv_type.setBackgroundResource(R.color.addPhone);
                    ((SearchHolder)holder).tv_form.setText("来自电话簿");
                    Glide.with(context).load(R.drawable.phone).into(((SearchHolder)holder).iv_type);
                    ((SearchHolder)holder).title.setVisibility(View.VISIBLE);
                    ((SearchHolder)holder).title.setText(dataList.get(position).getTitle());
                    break;
            }
            ((SearchHolder)holder).number.setText(dataList.get(position).getSearchNumber());
            ((SearchHolder)holder).itemView.setTag(dataList.get(position));
        }else {
            Log.i(TAG, "onBindViewHolder: 广告底部");
            final StandardNewsFeedAd feedAd = new StandardNewsFeedAd(context);
            feedAd.requestAd("85323922d8ba44babb65c7f439a09ae8", 1, new NativeAdListener() {
                @Override
                public void onNativeInfoFail(AdError adError) {

                }

                @Override
                public void onNativeInfoSuccess(List<NativeAdInfoIndex> list) {
                    feedAd.buildViewAsync(list.get(0), ((SearchAdvertHolder) holder).fl_advert.getWidth(), new AdListener() {
                        @Override
                        public void onAdError(AdError adError) {
                            Log.i(TAG, "onAdError: 广告获取失败"+adError.value());
                        }

                        @Override
                        public void onAdEvent(AdEvent adEvent) {

                        }

                        @Override
                        public void onAdLoaded() {

                        }

                        @Override
                        public void onViewCreated(View view) {
                            ((SearchAdvertHolder) holder).fl_advert.addView(view);
                        }
                    });
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setItemOnOnClickListener(SearchOnClickListener searchOnClickListener) {
        this.searchOnClickListener = searchOnClickListener;
    }

    public interface SearchOnClickListener {
        void ItemOnClickListener(SearchEntity searchEntity);
    }

    class SearchHolder extends RecyclerView.ViewHolder {

        private TextView tv_form;
        private TextView title;
        private TextView number;
        private ImageView iv_type;
        private View itemView;
        private TextView date;

        public SearchHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_item_search_title);
            number = (TextView) itemView.findViewById(R.id.tv_item_search_number);
            tv_form = (TextView) itemView.findViewById(R.id.tv_item_search_from);
            iv_type = (ImageView) itemView.findViewById(R.id.iv_item_search_type);
            date = (TextView) itemView.findViewById(R.id.tv_item_search_date);
            this.itemView = itemView;
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchOnClickListener.ItemOnClickListener((SearchEntity) v.getTag());
                }
            });
        }
    }

    class SearchAdvertHolder extends RecyclerView.ViewHolder {

        private FrameLayout fl_advert;

        public SearchAdvertHolder(View itemView) {
            super(itemView);
            fl_advert = (FrameLayout) itemView.findViewById(R.id.fl_search_advert);
        }
    }
}
