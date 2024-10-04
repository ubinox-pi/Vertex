package com.vertex.io;

public class FAQItem {
    private String question;
    private String answer;
    private boolean isExpanded;  // to track if the answer is visible or not

    public FAQItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.isExpanded = false;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
