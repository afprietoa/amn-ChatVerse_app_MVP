package aplicacionesmoviles.avanzado.todosalau.ejemplochat.Notificaciones;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.Chat.ChatActivity;
import aplicacionesmoviles.avanzado.todosalau.ejemplochat.R;

import java.util.Random;

public class MyFcmService extends FirebaseMessagingService{

    private static final String ADMIN_CHANNEL_ID = "ADMIN_CHANNEL_ID";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String title = ""+message.getNotification().getTitle();
        String body = ""+message.getNotification().getBody();
        String senderUid = ""+message.getData().get("senderUid");
        String notificationType = ""+message.getData().get("notificationType");

        showNotification(title, body, senderUid);
    }

    private void showNotification(String title, String body, String senderUid) {
        int notificationId = new Random().nextInt(3000);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        setupNotificationChannel(notificationManager);

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("uid", senderUid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notificationBuild = new NotificationCompat.Builder(this, ""+ADMIN_CHANNEL_ID)
                .setSmallIcon(R.drawable.chat_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notificationId, notificationBuild.build());
    }

    private void setupNotificationChannel (NotificationManager notificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    ADMIN_CHANNEL_ID,
                    "CHAT_CHANNEL",
                    NotificationManager.IMPORTANCE_HIGH
            );

            notificationChannel.setDescription("Show Chat Notification");
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
