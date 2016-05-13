package moe.haruue.redrockexam.util.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import moe.haruue.redrockexam.util.abstracts.HaruueActivity;
import moe.haruue.redrockexam.util.notification.NotificationUtils;

public class NotificationTestActivity extends HaruueActivity {

    Button showNotificationButton;
    Button showNotificationHeadsUpButton;
    Listener listener = new Listener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        showNotificationButton = $(R.id.button_show_notification);
        showNotificationButton.setOnClickListener(listener);
        showNotificationHeadsUpButton = $(R.id.button_show_notification_heads_up);
        showNotificationHeadsUpButton.setOnClickListener(listener);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, NotificationTestActivity.class);
        context.startActivity(starter);
    }

    protected class Listener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_show_notification:
                    showNotification();
                    break;
                case R.id.button_show_notification_heads_up:
                    showNotificationHeadsUp();
                    break;
            }
        }
    }

    protected void showNotification() {
        NotificationUtils.Builder builder = new NotificationUtils.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setTickerTextMessage("This is a Test Notification")
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setSubText("this is sub text");
        builder.build().setId((int) (Math.random() * 1000)).show();
    }

    protected void showNotificationHeadsUp() {
        NotificationUtils.Builder builder = new NotificationUtils.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setTickerTextMessage("This is a Test Notification")
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setSubText("this is sub text")
                .setHeadsUpMode();
        builder.build().setId((int) (Math.random() * 1000)).show();
    }
}
