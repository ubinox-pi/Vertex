package com.vertex.io;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {
    private List<FAQItem> faqList;

    public FAQAdapter(List<FAQItem> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQItem faqItem = faqList.get(position);
        holder.questionText.setText(faqItem.getQuestion());
        holder.answerText.setText(faqItem.getAnswer());

        boolean isExpanded = faqItem.isExpanded();
        holder.answerLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // Toggle expansion when a question is clicked
        holder.itemView.setOnClickListener(v -> {
            faqItem.setExpanded(!faqItem.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        TextView answerText;
        LinearLayout answerLayout;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            answerText = itemView.findViewById(R.id.answerText);
            answerLayout = itemView.findViewById(R.id.answerLayout);
        }
    }
}

