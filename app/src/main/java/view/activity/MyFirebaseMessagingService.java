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
        Log.d(TAG, "From11: " + remoteMessage.getFrom());
        Log.d(TAG, "From111: " + remoteMessage.getData());
     Map<String, String> data = remoteMessage.getData();
//             Log.d(TAG, "From111111: " + remoteMessage.getNotification().getBody());
//
//             if(new LoginSession(context).IsLogin(context)) {
////                 firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
////                 currentUsrId = firebaseUser.getUid();
//                 sessionParam=new LoginSession(this);
//
//                String currentUsrId= sessionParam.usr_id;
//                 String username= sessionParam.fname;
//                 String userpic= sessionParam.usr_pic;
//
//                 Log.e("","userid= "+currentUsrId+" username= "+username+" userpic"+userpic) ;
//
//                }
//         String data=remoteMessage.getData().toString();
//         String sTitle="",sMsg="",imageUrl="";
//         if (!TextUtils.isEmpty(data)&&data.contains("{")&&data.contains("}"))
//         {
//             try {
////                    String data1=remoteMessage.getNotification().getBody();
//                              data=data.substring(1, data.length()-1);
//                               String[] data1 = data.split(",");
//                              String urlPart= data1[0];
//                              String titlePart= data1[1];
//                              String messagePart= data1[2];
//                             String[]  imageUrl1 = urlPart.split("=");
//                            imageUrl= imageUrl1[1];
//                            String[]  titlePart1 = titlePart.split("=");
//                            sTitle= titlePart1[1];
//                            String[]  messagePart1 = messagePart.split("=");
//                            sMsg= messagePart1[1];
//
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }
//
//             createNotification(sTitle,sMsg);
//
//             if(!TextUtils.isEmpty(sessionParam.usr_id)) {
//                 DatabaseReference notification = FirebaseDatabase.getInstance().getReference("NOTIFICATION");
//                 SaveNotificationBean notiBean = new SaveNotificationBean(sessionParam.usr_id, sessionParam.fname, sessionParam.usr_pic, sTitle, sMsg, imageUrl);
//
//                 notification.child(sessionParam.usr_id).push().setValue(notiBean);
//             }
//         }
//         else
//         {
//             createNotification(getString(R.string.app_name),remoteMessage.getNotification().getBody());
//         }

//     createNotification(getString(R.string.app_name),remoteMessage.getNotification().getBody());//from FCM
     createNotification(getString(R.string.app_name),data.get("title"));//From postman emoteMessage.getData()

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
