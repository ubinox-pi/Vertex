package com.vertex.io;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * TODO: document your custom view class.
 */
public class withdrawal_list extends ArrayAdapter<withdraw_class> {

    public withdrawal_list(Context context, List<withdraw_class> withdrawals) {
        super(context, 0, withdrawals);
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sample_withdrawal_list, parent, false);
        }

        withdraw_class withdrawal = getItem(position);

        TextView withdrawalId = convertView.findViewById(R.id.withdrawalId);
        TextView withdrawalUpiId = convertView.findViewById(R.id.withdrawalUpiId);
        TextView withdrawalAmount = convertView.findViewById(R.id.withdrawalAmount);
        TextView withdrawalStatus = convertView.findViewById(R.id.withdrawalStatus);
        TextView withdrawalTxnId = convertView.findViewById(R.id.withdrawalTxnId);
        TextView withdrawalDate = convertView.findViewById(R.id.withdrawalDate);

        withdrawalId.setText("ID: " + withdrawal.getId());
        withdrawalUpiId.setText("UPI ID: " + withdrawal.getUpiId());
        withdrawalAmount.setText("Amount: $" + withdrawal.getAmount());
        withdrawalStatus.setText("Status: " + withdrawal.getStatus());
        withdrawalTxnId.setText("Txn ID: " + withdrawal.getTxnId());
        withdrawalDate.setText("Date: " + withdrawal.getDate());

        return convertView;
    }
}