package com.vertex.io;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class leaderboardAdapter extends ArrayAdapter<leaderBoardData> {

    private Context context;
    private List<leaderBoardData> leader;
    TextView name,point;
    ImageView imageView;


    public leaderboardAdapter(@NonNull Context context, List<leaderBoardData> list) {
        super(context,R.layout.leaderboard_list, list);
        this.context = context;
        this.leader = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.leaderboard_list, parent, false);
        }

        leaderBoardData leaderBoardData = leader.get(position);
        name = convertView.findViewById(R.id.profileName);
        point = convertView.findViewById(R.id.profilePoints);
        imageView = convertView.findViewById(R.id.profile);
        name.setText(leaderBoardData.getName());
        point.setText(String.valueOf(leaderBoardData.getCoin()));
        String link = leaderBoardData.getUrl();
        Glide.with(convertView)
                .load(link)
                .apply(new RequestOptions()
                        .placeholder(com.google.firebase.inappmessaging.display.R.drawable.image_placeholder) // Add a placeholder image
                        .error(R.drawable.error) // Add an error image
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(Target.SIZE_ORIGINAL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // Log the error or handle it
                        Log.e(TAG,"An error occurred: ", e);
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(imageView);


        return convertView;
    }
}
