package sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.data;

import android.text.Spannable;

public class MessageData {
    private final String text;
    private final String userName;
    private final int textGravity, textColor, userColor;
    private long id = -1;

    public MessageData(String text, String userName, int textGravity, int textColor, int userColor) {
        this.text = text;
        this.userName = userName;
        this.textGravity = textGravity;
        this.textColor = textColor;
        this.userColor = userColor;
    }

    public String getText() {
        return text;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public int getTextColor() {
        return textColor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserColor() {
        return userColor;
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "text='" + text + '\'' +
                ", textGravity=" + textGravity +
                ", textColor=" + textColor +
                ", id=" + id +
                '}';
    }
}
