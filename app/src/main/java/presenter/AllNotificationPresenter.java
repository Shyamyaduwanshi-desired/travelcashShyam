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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import constant.AppData;
import model.HistoryModel;
import model.NotificationBean;

public class AllNotificationPresenter {
    private Context context;
    private NotificationInfo notiInfo;
    private AppData appData;

    public AllNotificationPresenter(Context context, NotificationInfo notiInfo) {
        this.context = context;
        this.notiInfo = notiInfo;
        appData = new AppData(context);
    }

    public interface NotificationInfo {
        void success(ArrayList<NotificationBean> response);

        void error(String response);

        void fail(String response);
    }
    public void GetAllNotification() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Get Transaction Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<NotificationBean> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "getUserNotification", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        NotificationBean bean;
                        JSONArray jsArrayNoti = reader.getJSONArray("data");
                        for (int count = 0; count < jsArrayNoti.length(); count++) {
                            JSONObject object = jsArrayNoti.getJSONObject(count);
                            bean=new NotificationBean();
                            String date = object.getString("date");
                            String agent_id = object.getString("agent_id");
                            String user_message = object.getString("user_message");
                            String agent_image = object.getString("agent_image");
                            String agent_name = object.getString("agent_name");


                            bean.setDate(date);
                            bean.setAgent_id(agent_id);
                            bean.setUser_message(user_message);
                            bean.setAgent_image(agent_image);
                            bean.setAgent_name(agent_name);
                            list.add(bean);
                        }

                        notiInfo.success(list);
                    } else if (status == 0) {
                        notiInfo.error(reader.getString("message"));
                    }
                }  catch (JSONException e) {
                    e.printStackTrace();
                    notiInfo.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                notiInfo.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }


}
