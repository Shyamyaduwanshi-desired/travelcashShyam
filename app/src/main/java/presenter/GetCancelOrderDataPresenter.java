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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import constant.AppData;
import model.GetCancelOrderBean;
import model.HistoryModel;

public class GetCancelOrderDataPresenter {
    private Context context;
    private CancelHistoryInfo history;
    private AppData appData;

    public GetCancelOrderDataPresenter(Context context, CancelHistoryInfo history) {
        this.context = context;
        this.history = history;
        appData = new AppData(context);
    }

    public interface CancelHistoryInfo {
        void successCancelled(ArrayList<GetCancelOrderBean> response);

        void error(String response);

        void fail(String response);
    }

    public void getCancelledHistory() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Get Transaction Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<GetCancelOrderBean> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "getCancelledAndRejectedRequest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONArray cancelledTrans = reader.getJSONArray("result");
                        GetCancelOrderBean cancelBean;
                        for (int count = 0; count < cancelledTrans.length(); count++) {
                            cancelBean=new GetCancelOrderBean();
                            JSONObject object = cancelledTrans.getJSONObject(count);

                            String agent_recieved_request_id = object.getString("agent_recieved_request_id");
                            String user_request_user_id = object.getString("user_request_user_id");
                            String user_request_amount = object.getString("user_request_amount");
                            String agent_qr_code = object.getString("agent_qr_code");
                            String user_request_agent_id = object.getString("user_request_agent_id");
                            String request_status = object.getString("request_status");
                            String transaction_status = object.getString("transaction_status");
                            String request_date = object.getString("request_date");
                            String transaction_id = object.getString("transaction_id");
                            String refund_id = object.getString("refund_id");
                            String request_id = object.getString("request_id");
                            String is_purchase = object.getString("is_purchase");
                            String purchase_limit = object.getString("purchase_limit");
                            String is_promos = object.getString("is_promos");
                            String promos_discount = object.getString("promos_discount");
                            String request_updated_on = object.getString("request_updated_on");
                            String commission_amount = object.getString("commission_amount");
                            String status1 = object.getString("status");

                            cancelBean.setAgent_recieved_request_id(agent_recieved_request_id);
                            cancelBean.setUser_request_user_id(user_request_user_id);
                            cancelBean.setUser_request_amount(user_request_amount);
                            cancelBean.setAgent_qr_code(agent_qr_code);
                            cancelBean.setUser_request_agent_id(user_request_agent_id);
                            cancelBean.setRequest_status(request_status);
                            cancelBean.setTransaction_status(transaction_status);
                            cancelBean.setRequest_date(request_date);
                            cancelBean.setTransaction_id(transaction_id);
                            cancelBean.setRefund_id(refund_id);
                            cancelBean.setRequest_id(request_id);
                            cancelBean.setIs_purchase(is_purchase);
                            cancelBean.setPurchase_limit(purchase_limit);
                            cancelBean.setIs_promos(is_promos);
                            cancelBean.setPromos_discount(promos_discount);
                            cancelBean.setRequest_updated_on(request_updated_on);
                            cancelBean.setCommission_amount(commission_amount);
                            cancelBean.setStatus(status1);

                            list.add(cancelBean);
                        }

                        history.successCancelled(list);
                    } else if (status == 0) {
                        history.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    history.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                history.fail("Server Error.\n Please try after some time.");
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
