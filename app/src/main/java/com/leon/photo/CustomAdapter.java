package com.leon.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    ArrayList<MainActivity.photoFeedBack> photosFeedBack;
    Context context;

    public CustomAdapter(ArrayList<MainActivity.photoFeedBack> photosFeedBack, Context context) {
        this.photosFeedBack = photosFeedBack;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.imageView.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
            HighQualityFragment highQualityFragment = HighQualityFragment.newInstance(photosFeedBack.get(viewType).url);
            highQualityFragment.show(fragmentTransaction, "");
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MainActivity.photoFeedBack photoFeedBack = photosFeedBack.get(position);
        holder.textView.setText(photoFeedBack.title);
        Picasso.get().load(photoFeedBack.thumbnailUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photosFeedBack.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        @SuppressLint("NewApi")
        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            imageView = itemView.findViewById(R.id.image_view);
            linearLayout = itemView.findViewById(R.id.linear_layout);
        }
    }
}
