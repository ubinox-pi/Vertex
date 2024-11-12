package com.vertex.io;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class ChatAdapter extends ArrayAdapter<chat> {
    public ChatAdapter(Context context, int resource, List<chat> objects) {
        super(context, resource, objects);
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.tickets_chat, parent, false);
            }
            chat chat = getItem(position);
            TextView yourMessage = convertView.findViewById(R.id.your_message);
            TextView adminMessage = convertView.findViewById(R.id.admin_message);

            assert chat != null;
            yourMessage.setText(chat.getYourMessage());
            adminMessage.setText(chat.getAdminMessage());

            if (yourMessage.getText().toString().equals("")){
                yourMessage.setVisibility(View.GONE);
            } else {
                yourMessage.setVisibility(View.VISIBLE);
            }
            if (adminMessage.getText().toString().equals("")){
                adminMessage.setVisibility(View.GONE);
            } else {
                adminMessage.setVisibility(View.VISIBLE);
            }

            return convertView;
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Error");
            builder.setMessage(e.getMessage());
            builder.setCancelable(false);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.cancel());
            builder.create().show();
            return convertView;
        }

    }
}
