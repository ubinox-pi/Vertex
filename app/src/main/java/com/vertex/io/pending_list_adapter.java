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

public class pending_list_adapter extends ArrayAdapter<pending_list_data> {

    private Context context;
    private List<pending_list_data> pendingListData;
    String ID;
    TextView taskName,taskDescription,status;
    ImageView taskImage;
    String link;

    public pending_list_adapter(@NonNull Context context, List<pending_list_data> pendingListData) {
        super(context,R.layout.pending_tasks, pendingListData);
        this.context = context;
        this.pendingListData = pendingListData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.pending_tasks, parent, false);
        }

        pending_list_data pendingList = pendingListData.get(position);
        taskName = convertView.findViewById(R.id.Short);
        taskDescription = convertView.findViewById(R.id.Long);
        status = convertView.findViewById(R.id.status);
        ID = pendingList.Id;
        taskImage = convertView.findViewById(R.id.images);
        taskName.setText(pendingList.name);
        taskDescription.setText(pendingList.description);
        link = pendingList.Imagelink;
        if (pendingList.isPending){
            status.setText("Pending");
            status.setTextColor(context.getResources().getColor(org.imaginativeworld.oopsnointernet.R.color.flat_yellow_1));
        }
        else {
            status.setText("Completed");
            status.setTextColor(context.getResources().getColor(org.imaginativeworld.oopsnointernet.R.color.flat_green_1));
        }

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
                }).into(taskImage);

        return  convertView;
    }
}
