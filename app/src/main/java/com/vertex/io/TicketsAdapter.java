package com.vertex.io;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class TicketsAdapter extends ArrayAdapter<Tickets> {
    public TicketsAdapter(Context context, int resource, List<Tickets> objects) {
        super(context, resource, objects);
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_support_item, parent, false);
        }
        Tickets tickets = getItem(position);
        TextView Id = convertView.findViewById(R.id.ticket_id);
        TextView date = convertView.findViewById(R.id.ticket_date);
        TextView status = convertView.findViewById(R.id.ticket_status);
        TextView subject = convertView.findViewById(R.id.ticket_subject);
        TextView description = convertView.findViewById(R.id.ticket_description);
        Button view = convertView.findViewById(R.id.btn_view_ticket);

        assert tickets != null;
        Id.setText("Ticket Id: "+tickets.getTicketID());
        date.setText("Date: "+tickets.getDate());
        status.setText("Status: "+tickets.getStatus());
        if (tickets.getStatus().equals("Open")) {
            status.setTextColor(getContext().getResources().getColor(R.color.yellow));
        } else {
            status.setTextColor(getContext().getResources().getColor(R.color.green));
        }
        subject.setText("Subject: "+tickets.getSubject());
        description.setText("Description: "+tickets.getDescription());

        view.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ViewTicket.class)
                    .putExtra("ticketID", tickets.getTicketID());
            getContext().startActivity(intent);
        });

        return convertView;

    }

}
