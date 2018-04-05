package com.mrg.mynotbook.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.mrg.mynotbook.R;
import com.mrg.mynotbook.model.entities.Diary;

import com.mrg.mynotbook.utils.StringUtils;

import java.util.List;

/**
 * Created by MrG on 2017-05-08.
 */
public class DiaryListAdapter extends RecyclerView.Adapter<DiaryListAdapter.DiaryHolder> {


    public static final String TAG = "mrg";
    private List<Diary> list;
    private LayoutInflater inflater;
    private static final int HAPPY = 200;
    private static final int COMMONLY = 201;
    private static final int BAD = 202;
    private static final int SUNNY = 300;
    private static final int CLOUDY = 301;
    private static final int RAIN = 302;
    private static final int SNOW = 303;
    private OnDiaryItemClickListente clickListenter;

        private Context context;


    public DiaryListAdapter(Context context, List<Diary> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;


    }

    public void addData(Diary diary) {

        list.add(0, diary);
        notifyItemRangeChanged(0, list.size());

    }

    public void removeData(int position) {
        list.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());

    }

    public void upData(int position, Diary diary) {
        list.set(position, diary);

        notifyItemChanged(position);

    }


    public interface OnDiaryItemClickListente {
        void onDiaryItemClickListente(String id, String position);

        void deleteDiaryItemClickListente(String id, String position);
    }

    public void onItemClickListente(OnDiaryItemClickListente onDiaryItemClickListente) {

        clickListenter = onDiaryItemClickListente;
    }

    @Override
    public DiaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_diary_list, parent, false);
        return new DiaryHolder(view);
    }

    @Override

    public void onBindViewHolder(DiaryHolder holder, int position) {

        holder.tv_day.setText(list.get(position).getDay());
        String month = list.get(position).getMonth();
        holder.tv_month.setText( month.substring(month.indexOf("/")+1,month.length())+"月");
        holder.tv_number.setText(StringUtils.getNumber(list.get(position).getNumber()));
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_week.setText(list.get(position).getWeek());
        holder.tv_id.setText(list.get(position).getId());
        holder.tv_time.setText(list.get(position).getTime());
        holder.tv_location.setText(list.get(position).getLocation());
        holder.view.setTag(R.id.tag_id, list.get(position).getId());
        holder.view.setTag(R.id.tag_position, position + "");
        holder.btnDeleteView.setTag(R.id.tag_id, list.get(position).getId());
        holder.btnDeleteView.setTag(R.id.tag_position, position + "");

        //心情
        switch (list.get(position).getMood()) {

            case HAPPY:
                holder.img_mood.setBackgroundResource(R.drawable.ic_diary_bar_happy);

                break;
            case COMMONLY:
                holder.img_mood.setBackgroundResource(R.drawable.ic_diary_bar_commonly);
                break;
            case BAD:
                holder.img_mood.setBackgroundResource(R.drawable.ic_diary_bar_bad);
                break;

        }

        //天气
        switch (list.get(position).getWeather()) {
            case SUNNY:
                holder.img_weather.setBackgroundResource(R.drawable.ic_diary_bar_sunny);
                Glide.with(context).load(R.drawable.diary_snuuy).into(holder.weather_back);
                break;
            case CLOUDY:
                holder.img_weather.setBackgroundResource(R.drawable.ic_diary_bar_cloudy);
                Glide.with(context).load(R.drawable.diary_cloudy).into(holder.weather_back);

                break;

            case RAIN:
                holder.img_weather.setBackgroundResource(R.drawable.ic_diary_bar_rain);

                Glide.with(context).load(R.drawable.diary_rain).into(holder.weather_back);
                break;
            case SNOW:
                holder.img_weather.setBackgroundResource(R.drawable.ic_diary_bar_sonw);
                Glide.with(context).load(R.drawable.diary_sonw).into(holder.weather_back);
                break;
        }
        //收藏按钮

        if (list.get(position).getCollection() == 100) {
            holder.img_collection.setBackgroundResource(R.drawable.collno);
        } else {
            holder.img_collection.setBackgroundResource(R.drawable.collyes);
        }

        //显示图片
        if (!list.get(position).getPhoto().equals("")) {
            holder.ll_photo.setVisibility(View.VISIBLE);

            String photo = list.get(position).getPhoto();
            //图片异步加载
            RequestOptions requestOptions = new RequestOptions();

            requestOptions.error(R.drawable.shape_oval_normal);
            Glide.with(context)
                    .load(photo)
                    .apply(requestOptions)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(holder.img_photo);
        }else {
            holder.ll_photo.setVisibility(View.GONE);

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DiaryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_id;
        public TextView tv_day;
        public TextView tv_month;
        public TextView tv_week;
        public TextView tv_time;
        public TextView tv_title;
        public TextView tv_number;
        public TextView tv_location;
        public ImageView img_weather;
        public ImageView img_collection;
        public ImageView img_mood;
        public ImageView img_photo;

        public ImageView img_photo_1;
        public View view;
        public View btnDeleteView;
        public LinearLayout ll_photo;
        public ImageView weather_back;



        public DiaryHolder(View itemView) {
            super(itemView);
            tv_day = (TextView) itemView.findViewById(R.id.tv_day);
            tv_month = (TextView) itemView.findViewById(R.id.tv_month);
            tv_week = (TextView) itemView.findViewById(R.id.tv_week);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_title = (TextView) itemView.findViewById(R.id.tv_diary_title);
            tv_number = (TextView) itemView.findViewById(R.id.tv_diary_number);
            tv_id = (TextView) itemView.findViewById(R.id.tv_diary_id);
            tv_location = (TextView) itemView.findViewById(R.id.tv_diary_location);

            img_weather = (ImageView) itemView.findViewById(R.id.img_weather);
            img_collection = (ImageView) itemView.findViewById(R.id.img_collection);
            img_mood = (ImageView) itemView.findViewById(R.id.img_mood);
            img_photo = (ImageView) itemView.findViewById(R.id.img_photo);

            img_photo_1 = (ImageView) itemView.findViewById(R.id.img_photo_1);
            weather_back = (ImageView) itemView.findViewById(R.id.img_diary_weather_background);
            ll_photo = (LinearLayout) itemView.findViewById(R.id.ll_item_diary_list_photo);

            view = itemView.findViewById(R.id.cv_diary_content);
            btnDeleteView = itemView.findViewById(R.id.btn_diary_delete);
            view.setOnClickListener(this);
            btnDeleteView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.cv_diary_content:
                    if (clickListenter != null) {
                        clickListenter.onDiaryItemClickListente(view.getTag(R.id.tag_id).toString(), view.getTag(R.id.tag_position).toString());
                    }
                    break;
                case R.id.btn_diary_delete:
                    if (clickListenter != null) {
                        clickListenter.deleteDiaryItemClickListente(view.getTag(R.id.tag_id).toString(), view.getTag(R.id.tag_position).toString());
                    }
                    break;


            }


        }
    }
}
