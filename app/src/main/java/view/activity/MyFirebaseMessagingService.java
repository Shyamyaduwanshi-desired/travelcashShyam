package view.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.travelcash.R;

import java.util.Map;

import constant.AppData;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
 String TAG=MyFirebaseMessagingService.class.getSimpleName();
 Context context;
 private AppData appData;
 Context mContext;
// FirebaseUser firebaseUser=null;
// String currentUsrId="";
//    LoginSession  sessionParam;
 @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context=this;
     appData = new AppData(context);
        Log.d(TAG, "string From: " + remoteMessage.toString());
        Log.d(TAG, "From11: " + remoteMessage.getFrom());
        Log.d(TAG, "From111: " + remoteMessage.getData());
//       Log.d(TAG, "title From111111: " + remoteMessage.getNotification().getTitle());
//       Log.d(TAG, " body From111111: " + remoteMessage.getNotification().getBody());

     Map<String, String> data = remoteMessage.getData();
//     createNotification(getString(R.string.app_name),remoteMessage.getNotification().getBody());//from FCM
//     createNotification(getString(R.string.app_name),data.get("title"));//From postman emoteMessage.getData()
//     createNotification(data.get("title"),data.get("message"));//From postman emoteMessage.getData()
//     createNotification(data.get("title"),data.get("message"));//From postman emoteMessage.getData()

    Getdata(remoteMessage);

    }
    private void createNotification(String title,String messageBody) {
        appData.setNotiClick("1");
        Intent intent=new Intent( this , HomeActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this)
                .setSmallIcon(R.drawable.ic_notification)
//                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mNotificationBuilder.build());

    }

    private void Getdata(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            appData.setNotiClick("1");
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
//            String role = remoteMessage.getData().get("flag");


            Intent intent1=null;// = new Intent( this , HomeActivity. class );
            String userID = appData.getUserID();
            String googleID = appData.getGoogleID();
            if(!userID.equals("NA")){
                intent1 = new Intent(this, HomeActivity.class);
            }else if(!googleID.equals("NA")){
                intent1 = new Intent(this, HomeActivity.class);

            }else {//not loggined in user
                appData.setNotiClick("0");
                intent1 = new Intent(this, LoginActivity.class);
            }

//            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//            intent.putExtra("flag", "order");
            sendNotification(title, message, intent1);

//            if (role.equals("user_newrequest")) {
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                intent.putExtra("flag", "order");
//                sendNotification(title, message, intent);
//            } else if (role.equals("request_complete") || role.equals("request_cancelled")) {
//                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//                intent.putExtra("flag", "history");
//                sendNotification(title, message, intent);
//            }else if (role.equals("request_rejected") || role.equals("request_setteled")) {
//                Intent intent = new Intent(getApplicationContext(), SettlementActivity.class);
//                sendNotification(title, message, intent);
//            }else {
//                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
//                sendNotification(title, message, intent);
//            }
        }
    }



    private void sendNotification(String title, String message, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "agent_notification";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "You have new notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }


}
