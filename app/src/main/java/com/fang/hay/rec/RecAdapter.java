package com.fang.hay.rec;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fang.hay.R;
import com.fang.hay.data.pojo.Music;

import java.util.ArrayList;

/**
 * @author fanglh
 * @date 2018/9/27
 */
public class RecAdapter extends RecyclerView.Adapter<RecAdapter.RecHolder> {

    private ArrayList<Music> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    public RecAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void setList(ArrayList<Music> list) {
        this.mList = list;
    }

    @Override
    public RecHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_rec_music, parent, false);
        final RecAdapter.RecHolder holder = new RecAdapter.RecHolder(view);
        if (mListener != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(pos);
                    }
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position).album_art).into(holder.iv);
        holder.tv1.setText(mList.get(position).title);
        holder.tv2.setText(mList.get(position).artist);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    final class RecHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv1;
        TextView tv2;

        public RecHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.rec_iv);
            tv1 = (TextView) view.findViewById(R.id.rec_tv1);
            tv2 = (TextView) view.findViewById(R.id.rec_tv2);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
