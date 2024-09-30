package com.vertex.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class AllTaskAdapter extends ArrayAdapter<AllTaskData> {

    private Context context;
    private List<AllTaskData> taskList;
    public String Id;

    public AllTaskAdapter(Context context, List<AllTaskData> AllDataTask) {
        super(context, R.layout.task_ui, AllDataTask);
        this.context = context;
        this.taskList = AllDataTask;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_ui, parent, false);
        }
        AllTaskData task = taskList.get(position);
        TextView taskName = convertView.findViewById(R.id.Short);
        TextView taskDescription = convertView.findViewById(R.id.Long);
        ImageView taskImage = convertView.findViewById(R.id.images);
        Id = task.Id;
        taskName.setText(task.name);
        taskDescription.setText(task.description);
        new DownloadImageTask(taskImage).execute(task.Imagelink);

        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlDisplay = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new URL(urlDisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
