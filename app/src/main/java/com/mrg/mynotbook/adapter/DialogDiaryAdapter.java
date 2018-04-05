package com.mrg.mynotbook.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrg.mynotbook.R;

/**
 * Created by MrG on 2017-05-06.
 */
public class DialogDiaryAdapter extends RecyclerView.Adapter<DialogDiaryAdapter.UserHolder> {

    public static final String TAG = "mrg";
    private int[] urlList = {R.drawable.diary_bg_one, R.drawable.diary_bg_1, R.drawable.diary_bg_2, R.drawable.diary_bg_3_1, R.drawable.diary_bg_4, R.drawable.diary_bg_5};
    private Boolean[] booleans = {false, false, false, false, false, false};
    private LayoutInflater inflater;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public DialogDiaryAdapter(Context context) {
        inflater = LayoutInflater.from(context);

    }

    public void setCover(int position) {
        for (int i = 0; i < booleans.length; i++) {

            if (booleans[i]){
                booleans[i]=false;
                notifyItemChanged(i);
                break;
            }
        }
        booleans[position]=true;
        notifyItemChanged(position);

    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_dialog_diary_cover, parent, false);
        return new UserHolder(view);
    }


    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
        holder.itemView.setBackgroundResource(urlList[position]);
        holder.itemView.setTag(position);
        if (booleans[position])
            holder.tv_show.setVisibility(View.VISIBLE);
        else
            holder.tv_show.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return urlList.length;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView itemView;
        public TextView tv_show;

        public UserHolder(View view) {
            super(view);
            itemView = (TextView) view.findViewById(R.id.img_item_dialog_cover);
            tv_show = (TextView) view.findViewById(R.id.tv_item_dialog_show);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick((int) view.getTag(), view);
            }
        }
    }


}
