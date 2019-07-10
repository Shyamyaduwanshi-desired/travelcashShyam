package view.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.travelcash.R;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
 String TAG=MyFirebaseMessagingService.class.getSimpleName();
 Context context;
 Context mContext;
// FirebaseUser firebaseUser=null;
// String currentUsrId="";
//    LoginSession  sessionParam;
 @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context=this;
        Log.d(TAG, "string From: " + remoteMessage.toString());
        Log.d(TAG, "From11: " + remoteMessage.getFrom());
        Log.d(TAG, "From111: " + remoteMessage.getData());
//       Log.d(TAG, "title From111111: " + remoteMessage.getNotification().getTitle());
//       Log.d(TAG, " body From111111: " + remoteMessage.getNotification().getBody());

     Map<String, String> data = remoteMessage.getData();

//     createNotification(getString(R.string.app_name),remoteMessage.getNotification().getBody());//from FCM
//     createNotification(getString(R.string.app_name),data.get("title"));//From postman emoteMessage.getData()
//     createNotification(data.get("title"),data.get("message"));//From postman emoteMessage.getData()
     createNotification(data.get("title"),data.get("message"));//From postman emoteMessage.getData()
    }
    
    private void createNotification(String title,String messageBody) {
        Intent intent = new Intent( this , HomeActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotificationBuilder.build());
    }
}
