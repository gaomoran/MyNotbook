package com.mrg.mynotbook.adapter;

import android.content.Context;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mrg.mynotbook.R;
import com.mrg.mynotbook.model.entities.Quite;
import com.mrg.mynotbook.utils.StringUtils;
import com.mrg.mynotbook.widget.DataAudioView;
import com.mrg.mynotbook.widget.TrigonView;

import java.io.IOException;
import java.util.List;

/**
 * Created by MrG on 2017-11-15.
 */
public class QuiteAdapter extends RecyclerView.Adapter<QuiteAdapter.Holder> {
    private LayoutInflater inflater;
    private Context context;
    private List<Quite> list;
    private MediaPlayer player;
    private View.OnClickListener playAudio;
    private String TAG = "mrg";
    private QuiteAdapterListener quiteAdapterListener;
    private  int quiteTextColor;
    private  int quiteAudioColor;
    private  int quiteNoteColor;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_AUDIO = 3;
    private static final int TYPE_NOTE = 4;
    private  int quiteImageColor;

    public QuiteAdapter(Context context, List<Quite> list) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
        player = new MediaPlayer();

        playAudio = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerAudio(v);
            }
        };

        quiteTextColor = ContextCompat.getColor(context, R.color.quiteTextColor);
        quiteAudioColor = ContextCompat.getColor(context, R.color.quiteAudioColor);
        quiteNoteColor = ContextCompat.getColor(context, R.color.quiteNoteColor);
        quiteImageColor = ContextCompat.getColor(context, R.color.quiteImageColor);
    }

    public void add(Quite newQuite) {
        list.add(newQuite);
        notifyItemInserted(list.size());

    }

    public void removeData(int position) {
        list.remove(position);

        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());

    }

    public void upDate(int position, Quite quite) {
        list.set(position, quite);
        notifyItemChanged(position);

    }

    @Override
    public int getItemViewType(int position) {
        switch (list.get(position).getType()) {
            case "T":
                return TYPE_TEXT;
            case "I":
                return TYPE_IMAGE;
            case "A":
                return TYPE_AUDIO;
            default:
                return TYPE_NOTE;

        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_TEXT:
                View view = inflater.inflate(R.layout.item_main_quite_text, parent, false);
                return new Holder(view);

            case TYPE_IMAGE:
                View imageView = inflater.inflate(R.layout.item_main_quite_image, parent, false);
                return new Holder(imageView);

            case TYPE_AUDIO:
                View audioView = inflater.inflate(R.layout.item_main_quite_audio, parent, false);
                return new Holder(audioView);

            default:
                View noteView = inflater.inflate(R.layout.item_main_quite_note, parent, false);
                return new Holder(noteView);


        }

    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String number = list.get(position).getNumber();
        switch (list.get(position).getType()) {

            case "T":
                holder.ll_audio.setVisibility(View.GONE);
                holder.ll_photo.setVisibility(View.GONE);
                holder.tv_number.setVisibility(View.VISIBLE);
                holder.tv_number.setText(number);
                holder.trigonView.setTrigonColor(quiteTextColor);
                break;
            case "I":
                holder.tv_number.setVisibility(View.GONE);
                holder.ll_audio.setVisibility(View.GONE);
                holder.ll_photo.setVisibility(View.VISIBLE);
                Glide.with(context).load(number).into(holder.img_photo);
                holder.trigonView.setTrigonColor(quiteImageColor);
                break;
            case "A":
                holder.tv_number.setVisibility(View.GONE);
                holder.ll_photo.setVisibility(View.GONE);
                holder.ll_audio.setVisibility(View.VISIBLE);
                String[] split = number.split("#");
                holder.audioView.setAudioPath(split[0]);
                holder.audioView.setAudioTime(split[1]);
                holder.ll_audio.setOnClickListener(playAudio);
                holder.trigonView.setTrigonColor(quiteAudioColor);
                break;
            case "N":
                holder.tv_number.setVisibility(View.VISIBLE);
                holder.ll_audio.setVisibility(View.GONE);
                holder.tv_number.setText(StringUtils.getNoteNumber(number));
                String noteImage = StringUtils.getNoteImage(number);
                if (!noteImage.equals("")) {
                    holder.ll_photo.setVisibility(View.VISIBLE);

                    Glide.with(context).load(noteImage).apply(new RequestOptions().centerCrop()).into(holder.img_photo);
                } else {
                    holder.ll_photo.setVisibility(View.GONE);
                }
                holder.trigonView.setTrigonColor(quiteNoteColor);
                break;


        }
        holder.tv_date.setText(list.get(position).getDate());
        holder.tv_time.setText(list.get(position).getTime());
        holder.tv_week.setText(list.get(position).getWeek());

        holder.btn_delete.setTag(R.id.tag_id, list.get(position).getId());
        holder.btn_delete.setTag(R.id.tag_position, holder.getLayoutPosition());
        list.get(position).setPosition(position + "");
        holder.ll_item.setTag(list.get(position));

    }

    private void playerAudio(View view) {
        DataAudioView data = (DataAudioView) view.findViewById(R.id.rich_edit_audio_audioView);
        data.setFocusable(true);
        data.setFocusableInTouchMode(true);
        data.requestFocus();
        final ImageView bg = (ImageView) view.findViewById(R.id.img_rich_audio_play_background);
        String audioPath = data.getAudioPath();


        if (player.isPlaying()) {
            player.stop();
            Glide.with(context).load(R.drawable.audio_play).into(bg);
            player = new MediaPlayer();

        } else {
            try {
                player.setDataSource(audioPath);

            } catch (IOException e) {
                e.printStackTrace();
            }
            player.prepareAsync();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    Glide.with(context).load(R.drawable.audio_play_now).into(bg);

                }
            });
        }
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                player = new MediaPlayer();
                Glide.with(context).load(R.drawable.audio_play).into(bg);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_date;
        public TextView tv_week;
        public TextView tv_time;
        public TextView tv_number;
        public ImageView img_photo;
        public ImageView img_photo_1;
        public LinearLayout ll_photo;
        public LinearLayout ll_audio;
        public DataAudioView audioView;
        public Button btn_delete;
        public LinearLayout ll_item;
        public TrigonView trigonView;


        public Holder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_main_quite_date);
            tv_week = (TextView) itemView.findViewById(R.id.tv_main_quite_week);
            tv_time = (TextView) itemView.findViewById(R.id.tv_main_quite_time);
            tv_number = (TextView) itemView.findViewById(R.id.tv_main_quite_number);
            btn_delete = (Button) itemView.findViewById(R.id.btn_main_quite_delete);
            btn_delete.setOnClickListener(this);
            img_photo = (ImageView) itemView.findViewById(R.id.img_photo);
            img_photo_1 = (ImageView) itemView.findViewById(R.id.img_photo_1);
            ll_photo = (LinearLayout) itemView.findViewById(R.id.ll_item_main_quite_photo);
            ll_audio = (LinearLayout) itemView.findViewById(R.id.ll_item_main_quite_audio);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_main_quite_item);
            audioView = (DataAudioView) itemView.findViewById(R.id.rich_edit_audio_audioView);
            trigonView = (TrigonView) itemView.findViewById(R.id.tv_trigon);
            ll_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_main_quite_delete:
                    quiteAdapterListener.deleteItem(v.getTag(R.id.tag_id).toString(), v.getTag(R.id.tag_position).toString());
                    break;
                case R.id.ll_main_quite_item:
                    quiteAdapterListener.openItem((Quite) v.getTag());
                    break;
            }
        }
    }


    public void onQuiteAdapterListener(QuiteAdapterListener quiteAdapterListener) {
        this.quiteAdapterListener = quiteAdapterListener;
    }

    public interface QuiteAdapterListener {

        void deleteItem(String id, String position);

        void openItem(Quite quite);
    }
}
