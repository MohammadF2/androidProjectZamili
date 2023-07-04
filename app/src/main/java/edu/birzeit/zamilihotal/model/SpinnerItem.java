package edu.birzeit.zamilihotal.model;

public class SpinnerItem {
    private int drawable;
    private String text;

    public SpinnerItem(int drawable, String text) {
        this.drawable = drawable;
        this.text = text;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
