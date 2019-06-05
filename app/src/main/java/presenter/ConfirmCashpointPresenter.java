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
import constant.OrderData;
import model.AddMoneyModel;

public class ConfirmCashpointPresenter {
    private Context context;
    private Cashpoint cashpoint;
    private AppData appData;
    private OrderData orderData;

    public ConfirmCashpointPresenter(Context context, Cashpoint cashpoint) {
        this.context = context;
        this.cashpoint = cashpoint;
        appData = new AppData(context);
        orderData = new OrderData(context);
    }

    public interface Cashpoint{
        void success(String response);
        void error(String response);
        void fail(String response);
    }

    public void sendRequest() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "sendRequestToAgent", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        cashpoint.success(reader.getString("message"));
                    }else if(status == 0){
                        cashpoint.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    cashpoint.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                cashpoint.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("agent_id", orderData.agentID());
                params.put("request_amount", orderData.requestAmount());
                params.put("is_purchase ", "" + orderData.flagPurchase());
                params.put("purchase_limit ",  orderData.purchaseAmount());
                params.put("is_promos  ",  "" + orderData.flagPromos());
                params.put("promos_discount  ", orderData.promoDiscount());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
