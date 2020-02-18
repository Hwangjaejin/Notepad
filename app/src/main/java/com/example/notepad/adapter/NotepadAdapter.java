package com.example.notepad.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.notepad.MemoActivity;
import com.example.notepad.R;
import com.example.notepad.data.NotepadItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotepadAdapter extends RecyclerView.Adapter<NotepadAdapter.ViewHolder> {

    private ArrayList<NotepadItem> items;
    private Context mContext;

    public NotepadAdapter(ArrayList<NotepadItem> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notepad_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView title,detail,time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_src);
            title = itemView.findViewById(R.id.text_title);
            detail = itemView.findViewById(R.id.text_detail);
            time = itemView.findViewById(R.id.text_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent memoActivity = new Intent(v.getContext(), MemoActivity.class);
                    memoActivity.putExtra("id",items.get(position).getNotepadId());
                    v.getContext().startActivity(memoActivity);
                }
            });
        }
    }
    @Override
    public void onBindViewHolder(NotepadAdapter.ViewHolder holder, int position) {
        NotepadItem item = items.get(position);

        Glide.with(holder.itemView.getContext())
                .load(item.getImagePath())
                .into(holder.img);

        holder.title.setText(item.getTitle_text());
        holder.detail.setText(item.getDetail_text());
        holder.time.setText(item.getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
