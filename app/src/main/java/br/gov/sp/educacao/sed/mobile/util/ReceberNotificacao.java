package br.gov.sp.educacao.sed.mobile.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import br.gov.sp.educacao.sed.mobile.R;

public class ReceberNotificacao
        extends FirebaseMessagingService {

    String TAG = this.getClass().getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        NotificationManager nm = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        builder.setContentTitle(remoteMessage.getData().get("dado1"))
                .setContentText(remoteMessage.getData().get("dado2"))
                .setColor(getResources().getColor(R.color.azul_dia_letivo))
                .setSmallIcon(R.drawable.icone_notificacao)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = getApplicationContext().getString(R.string.default_notification_channel_id);

            NotificationChannel channel = new NotificationChannel(channelId, "Foto da Carteirinha", NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("Aprovado");

            nm.createNotificationChannel(channel);

            builder.setChannelId(channelId);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            builder.setContentTitle(remoteMessage.getData().get("dado1"))
                   .setContentText(remoteMessage.getData().get("dado2"));
        }

        Notification n = builder.build();

        nm.notify(R.drawable.icone_notificacao, n);
    }
}
