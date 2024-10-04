package com.vertex.io;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class pending_list_adapter extends ArrayAdapter<pending_list_data> {

    private Context context;
    private List<pending_list_data> pendingListData;
    String ID;
    TextView taskName,taskDescription,status;

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
        taskName.setText(pendingList.name);
        taskDescription.setText(pendingList.description);
        if (pendingList.isPending){
            status.setText("Pending");
            status.setTextColor(context.getResources().getColor(org.imaginativeworld.oopsnointernet.R.color.flat_yellow_1));
        }
        else {
            status.setText("Completed");
            status.setTextColor(context.getResources().getColor(org.imaginativeworld.oopsnointernet.R.color.flat_green_1));
        }

        return  convertView;
    }
}
