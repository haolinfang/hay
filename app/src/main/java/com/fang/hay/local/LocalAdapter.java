package com.fang.hay.local;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fang.hay.R;
import com.fang.hay.data.pojo.Music;

import java.util.ArrayList;

/**
 * @author fanglh
 * @date 2018/9/13
 */
public class LocalAdapter extends RecyclerView.Adapter<LocalAdapter.LocalHolder> {

    private Context mContext;
    private OnItemClickListener mListener;
    private ArrayList<Music> mList;

    public LocalAdapter() {
    }

    public LocalAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public void setList(ArrayList<Music> list) {
        this.mList = list;
    }

    public ArrayList<Music> getList() {
        return mList;
    }

    @Override
    public LocalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_local_song, parent, false);
        final LocalHolder holder = new LocalHolder(view);
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
    public void onBindViewHolder(LocalHolder holder, int position) {
        holder.tv1.setText(mList.get(position).title);
        holder.tv2.setText(mList.get(position).artist);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    final class LocalHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv1;
        TextView tv2;

        public LocalHolder(View view) {
            super(view);
            iv = (ImageView) view.findViewById(R.id.local_iv);
            tv1 = (TextView) view.findViewById(R.id.local_tv1);
            tv2 = (TextView) view.findViewById(R.id.local_tv2);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
