package com.mrg.mynotbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mrg.mynotbook.R;
import com.mrg.mynotbook.model.entities.ContentList;
import com.mrg.mynotbook.model.entities.PhoneUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrG on 2017-05-06.
 */
public class PhoneUserListAdapter extends RecyclerView.Adapter<PhoneUserListAdapter.UserHolder>{

    public static final String TAG="mrg";
    private static List<PhoneUser> userList;

    private LayoutInflater inflater;

    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public PhoneUserListAdapter(Context context,List<PhoneUser> phoneUserList) {
        inflater = LayoutInflater.from(context);
        this.userList=phoneUserList;
    }
    public void addData(PhoneUser phoneUser) {

        userList.add(phoneUser);
        userList.get(userList.size()-1).setPosition(userList.size() - 1 + "");
        notifyItemInserted(userList.size());

    }

    public void removeData(int position) {
        userList.remove(position);
        //删除一项后需要同步更新后面的position值
        for (int i = 0; i < userList.size(); i++) {
            userList.get(i).setPosition(i+"");

        }
        notifyItemRemoved(position);
    }
    public void upData(int position,PhoneUser phoneUser){
        userList.set(position, phoneUser);

        notifyItemChanged(position);

    }


    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_phone_user, parent, false);
        return new UserHolder(view);
    }


    @Override
    public void onBindViewHolder(UserHolder holder, int position) {
//        holder.setIsRecyclable(false);
        Log.i(TAG, "onBindViewHolder: 调用子页面"+position);
        String userName = userList.get(position).getUserName();
        holder.tv_userName.setText(userName);
        holder.tv_phone.setText(userList.get(position).getPhone());
        TextDrawable drawable1 = TextDrawable.builder()
                .buildRound((String) userName.subSequence(0, 1), mColorGenerator.getRandomColor());
        holder.imageView.setImageDrawable(drawable1);
        holder.viewById.setTag(userList.get(position));
        holder.bt_delete.setTag(userList.get(position));
        holder.bt_edit.setTag(userList.get(position));
        userList.get(position).setPosition(position+"");
    }



    public int getFirstPositionByChar(char sign) {
        if (sign == '#') {
            return 0;
        }
        Log.i(TAG, "getFirstPositionByChar: 获取角标："+sign);
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getSort().charAt(0) ==sign||userList.get(i).getSort().charAt(0)==(sign+32)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(PhoneUser data);
        void onDeleteClick(PhoneUser phoneUser);
        void onEditClick(PhoneUser phoneUser);


    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_userName;

        public TextView tv_phone;
        public ImageView imageView;
        public Button bt_delete;
        public Button bt_edit;
        public View viewById;

        public UserHolder(View view) {
            super(view);
            tv_userName = (TextView) view.findViewById(R.id.tv_phone_name);
            tv_phone = (TextView) view.findViewById(R.id.tv_phone_number);
            imageView = (ImageView) view.findViewById(R.id.img_user);
            bt_delete = (Button) view.findViewById(R.id.btn_phone_delete);
            bt_edit = (Button) view.findViewById(R.id.btn_phone_edit);
            viewById = view.findViewById(R.id.rl_phone_item);
            bt_edit.setOnClickListener(this);
            bt_delete.setOnClickListener(this);
            viewById.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.rl_phone_item:
                    if (mOnItemClickListener != null) {
                        //注意这里使用getTag方法获取数据
                        mOnItemClickListener.onItemClick((PhoneUser)view.getTag());
                    }
                    break;
                case R.id.btn_phone_delete:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onDeleteClick((PhoneUser) view.getTag());
                    }
                    break;
                case R.id.btn_phone_edit:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onEditClick((PhoneUser) view.getTag());
                    }
                    break;
            }
        }
    }



}
