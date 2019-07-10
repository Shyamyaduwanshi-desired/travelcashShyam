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

public class ScanActivityPresenter {
    private Context context;
    private Scan scan;
    private AppData appData;

    public ScanActivityPresenter(Context context, Scan scan) {
        this.context = context;
        this.scan = scan;
        appData = new AppData(context);
    }

    public interface Scan{
        void success(String response, String user_withdraw_id);
        void error(String response);
        void fail(String response);
    }

    public void verifyAgentQrCode(final String qr, final String amount, final String requestID, final String agentID, final String agent_received_request_id) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "verifyAgentQrCode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    Log.e("","json ="+reader.toString());
                    int status = reader.getInt("status");
                    if(status == 1){
                        scan.success(reader.getString("message"), reader.getString("user_withdraw_id"));
                    }else if(status == 0){
                        scan.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    scan.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                scan.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("agent_id", agentID);
                params.put("qrCodeInfo", qr);
                params.put("request_amount", amount);
                params.put("request_id", requestID);
                params.put("agent_recieved_request_id", agent_received_request_id);
                Log.e("","enput data verify QRcode= "+params.toString());
//                agent_id,user_id,qrCodeInfo,request_amount,request_id
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
