package com.mrg.mynotbook.adapter;

import android.content.Context;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import android.widget.FrameLayout;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.mrg.mynotbook.R;
import com.mrg.mynotbook.model.entities.ContentList;
import com.mrg.mynotbook.utils.StringUtils;
import com.mrg.mynotbook.widget.HintSideBar;

import com.xiaomi.ad.AdListener;
import com.xiaomi.ad.NativeAdInfoIndex;
import com.xiaomi.ad.NativeAdListener;
import com.xiaomi.ad.adView.StandardNewsFeedAd;
import com.xiaomi.ad.common.pojo.AdError;
import com.xiaomi.ad.common.pojo.AdEvent;

import java.util.ArrayList;

import java.util.List;

/**
 * Created by MrG on 2017-05-08.
 */
public class MainListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = "mrg";
    public static final int TYPE_DIARY = 1;
    public static final int TYPE_PHONE = 2;
    public static final int TYPE_NOTE = 3;
    private List<ContentList> list;
    private LayoutInflater inflater;
    private OnMainItemClickListener clickListenter;
    private View view;

    private boolean ad_flag = true;
    private int[] urlList = {R.drawable.diary_bg_one, R.drawable.diary_bg_1, R.drawable.diary_bg_2, R.drawable.diary_bg_3_1, R.drawable.diary_bg_4, R.drawable.diary_bg_5};
    private Context context;

    public MainListAdapter(Context context, List<ContentList> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(this.context);
        ContentList contentList = new ContentList();
        contentList.setType(4);
        list.add(0, contentList);

    }

    public void addDate(ContentList contentList) {


        list.add(contentList);
        list.get(list.size() - 1).setPosition(list.size() - 1 + "");
        Log.i(TAG, "addData: 长度：" + list.size());
        notifyItemInserted(list.size());
//        notifyItemRangeInserted(list.size()-diarry_bg_1, diarry_bg_1);

    }


    public void removeDate(int position) {

        list.remove(position);

        Log.i(TAG, "removeData: 删除标识：" + position);
        //删除一项后需要同步更新后面的position值
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setPosition(i + "");

        }
        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, list.size());
    }


    public void update(int position, ContentList contentList) {

        list.set(position, contentList);

        notifyItemChanged(position);

    }

    public interface OnMainItemClickListener {
        void onMainItemClickListener(ContentList contentList);

        void deleteItemClickListener(ContentList contentList);

        void editItemClickListener(ContentList contentList);
    }

    public void onItemClickListenter(OnMainItemClickListener onMainItemClickListener) {

        clickListenter = onMainItemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_DIARY) {
            view = inflater.inflate(R.layout.item_main_diary_content, parent, false);
            return new MainHolder(view);
        } else if (viewType == TYPE_NOTE) {
            view = inflater.inflate(R.layout.item_main_note_content, parent, false);
            return new NoteHolder(view);


        } else if (viewType == TYPE_PHONE) {
            view = inflater.inflate(R.layout.item_main_phone_content, parent, false);
            return new PhoneHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_main_index, parent, false);
            return new IndexHolder(view);

        }

    }

    @Override

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        //用来使一个子项独占一行
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == 0
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

//        holder.setIsRecyclable(false);
        if (holder instanceof MainHolder) {
            ((MainHolder) holder).mTitle.setText(list.get(position).getNumber());
            list.get(position).setPosition(position + "");
            ((MainHolder) holder).mDelete.setTag(list.get(position));
            ((MainHolder) holder).mEdit.setTag(list.get(position));
            ((MainHolder) holder).mItemContent.setTag(list.get(position));
            String cover = list.get(position).getCover();


            if (cover != "" && cover != null) {
                ((MainHolder) holder).imageview.setBackgroundResource(urlList[Integer.parseInt(cover)]);
            }
        } else if (holder instanceof NoteHolder) {
            ((NoteHolder) holder).mNumber.setText(StringUtils.getNoteNumber(list.get(position).getNumber()));
            list.get(position).setPosition(position + "");
            ((NoteHolder) holder).mDelete.setTag(list.get(position));
            ((NoteHolder) holder).mEdit.setTag(list.get(position));
            ((NoteHolder) holder).mItemContent.setTag(list.get(position));
        } else if (holder instanceof PhoneHolder) {
            ((PhoneHolder) holder).mTitle.setText(list.get(position).getNumber());
            list.get(position).setPosition(position + "");
            ((PhoneHolder) holder).mDelete.setTag(list.get(position));
            ((PhoneHolder) holder).mEdit.setTag(list.get(position));
            ((PhoneHolder) holder).mItemContent.setTag(list.get(position));
        } else {

            if (ad_flag) {
                ((IndexHolder) holder).startLoadAd();
                ad_flag = false;
            }

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mTypeico;
        public TextView mDelete;
        public TextView mEdit;
        public CardView mItemContent;
        public ImageView imageview;

        public MainHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_main_diary_title);
            mTypeico = (TextView) itemView.findViewById(R.id.tv_main_diary_type_ico);
            mDelete = (TextView) itemView.findViewById(R.id.tv_main_diary_delete);
            mEdit = (TextView) itemView.findViewById(R.id.tv_main_diary_edit);
            imageview = (ImageView) itemView.findViewById(R.id.iv_main_diary_content);
            mItemContent = (CardView) itemView.findViewById(R.id.cv_main_diary_content);
            mItemContent.setOnClickListener(this);
            mDelete.setOnClickListener(this);
            mEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tv_main_diary_delete:
                    if (clickListenter != null) {
                        clickListenter.deleteItemClickListener((ContentList) view.getTag());
                    }
                    break;
                case R.id.tv_main_diary_edit:
                    if (clickListenter != null) {
                        clickListenter.editItemClickListener((ContentList) view.getTag());

                    }
                    break;
                case R.id.cv_main_diary_content:
                    if (clickListenter != null) {
                        clickListenter.onMainItemClickListener((ContentList) view.getTag());
                    }

                    break;

            }

        }
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTypeico;
        public TextView mNumber;
        public TextView mDelete;
        public TextView mEdit;
        public CardView mItemContent;


        public NoteHolder(View itemView) {
            super(itemView);
            mNumber = (TextView) itemView.findViewById(R.id.tv_main_note_number);
            mTypeico = (TextView) itemView.findViewById(R.id.tv_main_note_type_ico);
            mDelete = (TextView) itemView.findViewById(R.id.tv_main_note_delete);
            mEdit = (TextView) itemView.findViewById(R.id.tv_main_note_edit);
            mItemContent = (CardView) itemView.findViewById(R.id.cv_main_note_content);
            mItemContent.setOnClickListener(this);
            mDelete.setOnClickListener(this);
            mEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tv_main_note_delete:
                    if (clickListenter != null) {
                        clickListenter.deleteItemClickListener((ContentList) view.getTag());
                    }
                    break;
                case R.id.tv_main_note_edit:
                    if (clickListenter != null) {
                        clickListenter.editItemClickListener((ContentList) view.getTag());

                    }
                    break;
                case R.id.cv_main_note_content:
                    if (clickListenter != null) {
                        clickListenter.onMainItemClickListener((ContentList) view.getTag());
                    }

                    break;

            }
        }
    }

    class PhoneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTitle;
        public TextView mTypeico;
        public TextView mDelete;
        public TextView mEdit;
        public CardView mItemContent;

        public PhoneHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.tv_main_phone_title);
            mTypeico = (TextView) itemView.findViewById(R.id.tv_main_phone_type_ico);
            mDelete = (TextView) itemView.findViewById(R.id.tv_main_phone_delete);
            mEdit = (TextView) itemView.findViewById(R.id.tv_main_phone_edit);
            mItemContent = (CardView) itemView.findViewById(R.id.cv_main_phone_content);
            mItemContent.setOnClickListener(this);
            mDelete.setOnClickListener(this);
            mEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.tv_main_phone_delete:
                    if (clickListenter != null) {
                        clickListenter.deleteItemClickListener((ContentList) view.getTag());
                    }
                    break;
                case R.id.tv_main_phone_edit:
                    if (clickListenter != null) {
                        clickListenter.editItemClickListener((ContentList) view.getTag());

                    }
                    break;
                case R.id.cv_main_phone_content:
                    if (clickListenter != null) {
                        clickListenter.onMainItemClickListener((ContentList) view.getTag());
                    }

                    break;

            }
        }
    }

    class IndexHolder extends RecyclerView.ViewHolder implements IndexAdapter.IndexAdapterListener {
        public ViewPager vp_index;
        private Handler handler;
        private int delay = 5000;
        private boolean isAutoPlay = false;
        private int next = -1;
        private int currentItem = 0;
        private Runnable task = new Runnable() {
            @Override
            public void run() {
                if (isAutoPlay) {
                    // 位置循环
                    if (currentItem == 3 || currentItem == 0) {
                        next = -next;
                    }
                    currentItem = currentItem + next;
                    // 正常每隔3秒播放一张图片
                    vp_index.setCurrentItem(currentItem);
                    handler.postDelayed(task, delay);
                } else {
                    // 如果处于拖拽状态停止自动播放，会每隔5秒检查一次是否可以正常自动播放。
//                    handler.postDelayed(task, 5000);
                }
            }
        };
        private IndexAdapter indexAdapter;


        public IndexHolder(View itemView) {
            super(itemView);
            vp_index = (ViewPager) itemView.findViewById(R.id.vp_main_index);

            indexAdapter = new IndexAdapter(context);
            indexAdapter.setIndexAdapterListener(this);
            vp_index.setAdapter(indexAdapter);
            handler = new Handler();


        }

        public void startLoadAd() {
            final StandardNewsFeedAd feedAd = new StandardNewsFeedAd(context);
            feedAd.requestAd("f3f7fbb2f06e5072dfa38478ff44ffc9", 3, new NativeAdListener() {
                @Override
                public void onNativeInfoFail(AdError adError) {
                    Log.i(TAG, "onNativeInfoFail: 广告出错" + adError.toString());
                }

                @Override
                public void onNativeInfoSuccess(List<NativeAdInfoIndex> list) {

                    for (int i = 0; i < list.size(); i++) {

                        final int finalI = i;
                        feedAd.buildViewAsync(list.get(i), vp_index.getWidth(), new AdListener() {


                            @Override
                            public void onAdError(AdError adError) {
                                Log.i(TAG, "onAdError: 加载广告错误" + adError.toString());
                            }

                            @Override
                            public void onAdEvent(AdEvent adEvent) {
                                Log.i(TAG, "onAdEvent: 获取广告");
                            }

                            @Override
                            public void onAdLoaded() {

                            }

                            @Override
                            public void onViewCreated(View view) {
//
                                indexAdapter.updateView(view, finalI + 1);
                            }
                        });

                    }
                }

            });
            handler.postDelayed(task, delay);

        }

        @Override
        public void isLoadAd() {
            isAutoPlay = true;
        }
    }

}
