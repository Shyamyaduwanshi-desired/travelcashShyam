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
import model.ShowWalletInfoModel;

public class ShowWalletInfoPresenter {
    private Context context;
    private WalletInfo walletInfo;

    public ShowWalletInfoPresenter(Context context, WalletInfo walletInfo) {
        this.context = context;
        this.walletInfo = walletInfo;
    }

    public interface WalletInfo{
        void success(ArrayList<ShowWalletInfoModel> arrayList, String walletAmount);
        void error(String response);
        void fail(String response);
    }

    public void ShowWalletInfo(final String userID) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Getting Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<ShowWalletInfoModel> arrayList = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showWallet", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if(status == 1){
                        arrayList.clear();
                        JSONArray jsonArray = reader.getJSONArray("request");
                        ShowWalletInfoModel bean;
                        for(int count =0 ;count<jsonArray.length(); count++){
                            bean=new ShowWalletInfoModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(count);

                            String amount = jsonObject.getString("amount");
                            String transaction_id = jsonObject.getString("transaction_id");
                            String request_status = jsonObject.getString("request_status");
                            String request_date = jsonObject.getString("request_date");
                            String request_id = jsonObject.getString("request_id");
                            String status1 = jsonObject.getString("status");

                            bean.setAmount(amount);
                            bean.setTransaction_id(transaction_id);
                            bean.setRequest_status(request_status);
                            bean.setRequest_date(request_date);
                            bean.setRequest_id(request_id);
                            bean.setStatus(status1);
                            arrayList.add(bean);
                        }

                        walletInfo.success(arrayList, reader.getString("userWallet"));
                    }else if(status == 0){
                       walletInfo.error(reader.getString("message"));
                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                    walletInfo.fail("Something went wrong. Please try after some time.");
                } 
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                walletInfo.fail("Server Error.\n Please try after some time.");
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
