package sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.contentprovider.DictProvider;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.contentprovider.IrcContentProvider;
import sample.valentinshelkov.com.sampleapplicationwithmapsircanimation.data.MessageData;

public class IrcFragment extends ListFragment {


    private static final String TAG = IrcFragment.class.getSimpleName();
    public static final int RANDOM_MESSAGES_SENDING_INTERVAL_SECONDS = 10;

    private AbsListView listView;
    private IrcAdapter adapter;
    private ScheduledFuture<?> sendRandomMessageFuture;

    public IrcFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor c = contentResolver.query(IrcContentProvider.CONTENT_URI, null, null, null, null);
        adapter = new IrcAdapter(getActivity(), c, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set the adapter
        listView = getListView();
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        setListAdapter(adapter);
        listView.setSelection(adapter.getCount() - 1);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        sendRandomMessageFuture = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new SendRandomMessage(activity, this), 0, RANDOM_MESSAGES_SENDING_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void onDetach() {
        adapter.unregisterContentObserver();
        if (sendRandomMessageFuture != null && (!sendRandomMessageFuture.isCancelled() && !sendRandomMessageFuture.isDone())) {
            sendRandomMessageFuture.cancel(false);
            sendRandomMessageFuture = null;
        }
        super.onDetach();
    }

    private static class IrcAdapter extends CursorAdapter {
        private static final String TAG = IrcAdapter.class.getSimpleName();
        private final Context context;
        private final ChangeObserver observer;
        private final IrcFragment fragment;

        public IrcAdapter(Context context, Cursor cursor, IrcFragment fragment) {
            super(context, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            observer = new ChangeObserver(this);
            context.getContentResolver().registerContentObserver(IrcContentProvider.CONTENT_URI, true, observer);
            this.context = context;
            this.fragment = fragment;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            TextView newView = (TextView) LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            newView.setSingleLine(false);
            return newView;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView boundView = (TextView) view;
            MessageData messageData = getMessageData(cursor);
            if (messageData == null) return;
            boundView.setGravity(messageData.getTextGravity());
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                boundView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
            }
            boundView.setTextColor(messageData.getTextColor());
            String source = messageData.getUserName() + ": " + messageData.getText();
            Spannable text = new SpannableString(source);
            text.setSpan(new ForegroundColorSpan(messageData.getUserColor()), 0, messageData.getUserName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            boundView.setText(text);
        }

        private MessageData getMessageData(Cursor c) {
            String text = c.getString(c.getColumnIndex(IrcContentProvider.TEXT));
            String userName = c.getString(c.getColumnIndex(IrcContentProvider.USER_NAME));
            int gravity = c.getInt(c.getColumnIndex(IrcContentProvider.GRAVITY));
            int color = c.getInt(c.getColumnIndex(IrcContentProvider.COLOR));
            int userColor = c.getInt(c.getColumnIndex(IrcContentProvider.USER_COLOR));
            return new MessageData(text, userName, gravity, color, userColor);
        }

        protected void onContentChanged() {
            Cursor c = context.getContentResolver().query(IrcContentProvider.CONTENT_URI, null, null, null, null);
            changeCursor(c);
        }

        void unregisterContentObserver() {
            context.getContentResolver().unregisterContentObserver(observer);
        }

        private static class UserWrapper {

        }

        private static class ChangeObserver extends ContentObserver {
            private final IrcAdapter ircAdapter;

            public ChangeObserver(IrcAdapter ircAdapter) {
                super(new Handler());
                this.ircAdapter = ircAdapter;
            }

            @Override
            public boolean deliverSelfNotifications() {
                return true;
            }

            @Override
            public void onChange(boolean selfChange) {
                ircAdapter.onContentChanged();
            }

        }
    }

    private static class SendRandomMessage implements Runnable {
        private static final String TAG = SendRandomMessage.class.getSimpleName();
        private static final String[] MOCK_CHAT_USERS = {"Andrew", "Alla", "Nick", "Serge", "Valentine"};
        private static final int[] MOCK_CHAT_USERS_COLORS = {Color.YELLOW, Color.BLUE, Color.CYAN, Color.RED, Color.GREEN};
        private final Activity activity;
        private final IrcFragment ircFragment;

        private SendRandomMessage(Activity activity, IrcFragment ircFragment) {
            this.activity = activity;
            this.ircFragment = ircFragment;
        }

        public MessageData call() {
            ContentResolver contentResolver = activity.getContentResolver();
            Cursor c = contentResolver.query(DictProvider.CONTENT_URI, null, null, null, null);
            if (c.moveToFirst()) {
                StringBuilder builder = new StringBuilder();
                do {
                    builder.append(c.getString(c.getColumnIndex(DictProvider.WORD)));
                    builder.append(' ');
                } while (c.moveToNext());
                Random random = new Random();
                int i = random.nextInt(MOCK_CHAT_USERS.length - 1);
                MessageData messageData = new MessageData(builder.toString(), MOCK_CHAT_USERS[i], Gravity.LEFT, Color.DKGRAY, MOCK_CHAT_USERS_COLORS[i]);
                return messageData;
            }
            return null;
        }

        @Override
        public void run() {
            MessageData messageData = call();
            ContentResolver contentResolver = activity.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(IrcContentProvider.TEXT, messageData.getText());
            contentValues.put(IrcContentProvider.COLOR, messageData.getTextColor());
            contentValues.put(IrcContentProvider.USER_COLOR, messageData.getUserColor());
            contentValues.put(IrcContentProvider.USER_NAME, messageData.getUserName());
            contentValues.put(IrcContentProvider.GRAVITY, messageData.getTextGravity());
            Uri uri = contentResolver.insert(IrcContentProvider.CONTENT_URI, contentValues);
        }
    }
}
