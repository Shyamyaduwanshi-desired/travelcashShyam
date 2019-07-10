package presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import constant.AppData;

public class SendNotiToAgentPresenter {
    private Context context;
    private SendNoti sendNotification;
    private AppData appData;

    public SendNotiToAgentPresenter(Context context, SendNoti sendNotification) {
        this.context = context;
        this.sendNotification = sendNotification;
        appData = new AppData(context);
    }

    public interface SendNoti{
        void success(String response, String check);
        void error(String response, String check);
        void fail(String response, String check);
    }

    public void SendNotificationTOAgent(final String devicetoken) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.noti_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("success");
                    if(status == 1){
                        String msg=reader.getJSONArray("results").getJSONObject(0).getString("message_id");
                        Log.e("","shyam send noti= "+reader.getString("message"));
                        sendNotification.success(reader.getString("success"),msg);
                    }else if(status == 0){
                       sendNotification.error(reader.getString("message"),"no data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendNotification.fail("Something went wrong. Please try after some time.","token");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                sendNotification.fail("Server Error.\n Please try after some time.","token");
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("device_token", devicetoken);
                params.put("device_type", "android");

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);

//        postRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
