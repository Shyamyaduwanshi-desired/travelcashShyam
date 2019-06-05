package presenter;

import android.app.ProgressDialog;
import android.content.Context;

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
import model.RecentActivity;

public class RecentActivityPresenter {
    private Context context;
    private Wallet wallet;

    public RecentActivityPresenter(Context context, Wallet wallet) {
        this.context = context;
        this.wallet = wallet;
    }

    public interface Wallet{
        void success(ArrayList<RecentActivity> arrayList, String walletAmount);
        void error(String response);
        void fail(String response);
    }

    public void recentActivity(final String userID) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Getting Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<RecentActivity> arrayList = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showWallet", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        arrayList.clear();
                        JSONArray jsonArray = reader.getJSONArray("completedExchangeTransactions");
                        for(int count =0 ;count<jsonArray.length(); count++){
                            JSONObject jsonObject = jsonArray.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            String date = jsonObject.getString("user_exchange_request_date_time");
                            Date date_change = input.parse(date);
                            RecentActivity activity = new RecentActivity(jsonObject.getString("user_exchange_request_id"),jsonObject.getString("bank_name"), output.format(date_change), jsonObject.getString("request_amount"), "1");
                            arrayList.add(activity);
                        }

                        JSONArray json_array = reader.getJSONArray("cancelledExchangeTransacions");
                        for(int count =0 ;count<json_array.length(); count++){
                            JSONObject jsonObject = json_array.getJSONObject(count);
                            String date = jsonObject.getString("user_exchange_request_date_time");
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            Date date_change = input.parse(date);
                            RecentActivity activity = new RecentActivity(jsonObject.getString("user_exchange_request_id"),jsonObject.getString("bank_name"), output.format(date_change), jsonObject.getString("request_amount"), "0");
                            arrayList.add(activity);
                        }

                        wallet.success(arrayList, reader.getString("userWallet"));
                    }else if(status == 0){
                       wallet.error(reader.getString("message"));
                    }
                }catch (ParseException ex) {
                    ex.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    wallet.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                wallet.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", userID);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
