package moe.haruue.redrockexam.util.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.widget.RemoteViews;

/**
 * 兼容性的 Notification 工具类，使用参考 {@link Notification}
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class NotificationUtils {

    Notification notification;
    Context context;
    int id = 0;
    String tag = null;

    private NotificationUtils() {

    }

    public static class Builder {

        Notification.Builder builder;
        Context context;
        CharSequence contentText = "";
        CharSequence subText = "";
        RemoteViews contentViews;
        RemoteViews bigContentViews;
        int color = -1;
        String category = "";

        public Builder(Context context) {
            builder = new Notification.Builder(context);
            this.context = context;
        }

        public Builder setSmallIcon(@DrawableRes int res) {
            builder.setSmallIcon(res);
            return this;
        }

        public Builder setLargeIcon(@DrawableRes int res) {
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), res));
            return this;
        }

        public Builder setLargeIcon(Bitmap bitmap) {
            builder.setLargeIcon(bitmap);
            return this;
        }

        public Builder setAutoCancel(boolean isAutoCancel) {
            builder.setAutoCancel(isAutoCancel);
            return this;
        }

        public Builder setTickerTextMessage(CharSequence tickerTextMessage) {
            builder.setTicker("TickerText:" + tickerTextMessage);
            return this;
        }

        public Builder setContentTitle(CharSequence title) {
            builder.setContentTitle(title);
            return this;
        }

        public Builder setContentText(CharSequence text) {
            this.contentText = text;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.setContentText(text);
            } else {
                builder.setContentText(text + (subText.length() != 0 ? "\n" + subText : ""));
            }
            return this;
        }

        public Builder setSubText(CharSequence subText) {
            this.subText = subText;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.setSubText(subText);
            } else {
                builder.setContentText((contentText.length() != 0 ? contentText + "\n" : "") + subText);
            }
            return this;
        }

        public Builder setContentView(RemoteViews remoteViews) {
            this.contentViews = remoteViews;
            return this;
        }

        public Builder setBigContentView(RemoteViews remoteViews) {
            this.bigContentViews = remoteViews;
            return this;
        }

        public Builder setContentIntent(PendingIntent pendingIntent) {
            builder.setContentIntent(pendingIntent);
            return this;
        }

        public Builder setDeleteIntent(PendingIntent pendingIntent) {
            builder.setDeleteIntent(pendingIntent);
            return this;
        }

        public Builder setFullScreenIntent(PendingIntent pendingIntent, boolean highPriority) {
            builder.setFullScreenIntent(pendingIntent, highPriority);
            return this;
        }

        public Builder setHeadsUpMode() {
            builder.setFullScreenIntent(PendingIntent.getActivity(context, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT), true);
            return this;
        }

        public Builder setVisibility(int visibility) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setVisibility(visibility);
            }
            return this;
        }

        public Builder setColor(int color) {
            this.color = color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setColor(color);
            }
            return this;
        }

        public Builder setCatagory(String category) {
            this.category = category;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setCategory(category);
            }
            return this;
        }

        public Builder setAction(Notification.Action action) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                builder.addAction(action);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                builder.addAction(action.icon, action.title, action.actionIntent);
            }
            return this;
        }

        public Builder setActionForJellyBean(int icon, CharSequence title, PendingIntent intent) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                builder.addAction(icon, title, intent);
            }
            return this;
        }

        public Notification.Builder getBuilder() {
            return builder;
        }

        public NotificationUtils build() {
            NotificationUtils utils = new NotificationUtils();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                utils.notification = builder.build();
            } else {
                utils.notification = builder.getNotification();
            }
            if (contentViews != null) {
                utils.notification.contentView = contentViews;
            }
            if (bigContentViews != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    utils.notification.bigContentView = bigContentViews;
            }
            utils.context = context;
            return utils;
        }
    }

    public Notification getNotification() {
        return notification;
    }

    public NotificationUtils setId(int id) {
        this.id = id;
        return this;
    }

    public NotificationUtils setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public NotificationUtils show() {
        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.notify(tag, id, notification);
        return this;
    }

    public NotificationUtils cancel() {
        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.cancel(id);
        return this;
    }

    public static void cancelAll(Context context) {
        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.cancelAll();
    }

    public static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

}
