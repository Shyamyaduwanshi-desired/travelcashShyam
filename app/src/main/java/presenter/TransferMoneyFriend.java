package presenter;

import android.app.ProgressDialog;
import android.content.Context;

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

public class TransferMoneyFriend {
    private Context context;
    private Transfer transfer;
    private AppData appData;

    public TransferMoneyFriend(Context context, Transfer transfer) {
        this.context = context;
        this.transfer = transfer;
        appData = new AppData(context);
    }

    public interface Transfer{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void usingQR(final String user_id, final String amount) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Sending Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "transferToFriendUsingQrCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        transfer.success(reader.getString("message"));
                    }else if(status == 0){
                        transfer.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    transfer.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                transfer.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("friend_user_id", user_id);
                params.put("transfer_amount", amount);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void usingUsername(final String username, final String amount) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Sending Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "transferToFriendUsingUsername", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        transfer.success(reader.getString("message"));
                    }else if(status == 0){
                        transfer.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    transfer.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                transfer.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("friends_username", username);
                params.put("transfer_amount", amount);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
