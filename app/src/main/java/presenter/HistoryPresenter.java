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
import model.History;

public class HistoryPresenter {
    private Context context;
    private History history;
    private AppData appData;

    public HistoryPresenter(Context context, History history) {
        this.context = context;
        this.history = history;
        appData = new AppData(context);
    }

    public interface History {
        void success(ArrayList<model.History> response);

        void error(String response);

        void fail(String response);
    }

    public void getCompletedHistory() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Get Transaction Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<model.History> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "userHistory", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONArray withdrawTransactions = reader.getJSONArray("completedWithdrawTransactions");
                        for (int count = 0; count < withdrawTransactions.length(); count++) {
                            JSONObject object = withdrawTransactions.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("withdraw_date_time");
                            Date date_change = input.parse(date);
                            model.History his = new model.History(object.getString("user_withdraw_id"),output.format(date_change), "Total Cash Withdraw", time.format(date_change), object.getString("withdraw_amount"));
                            list.add(his);
                        }

                        JSONArray bankTransactions = reader.getJSONArray("completedTransferedToBankTransactions");
                        for (int count = 0; count < bankTransactions.length(); count++) {
                            JSONObject object = bankTransactions.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("user_transfered_to_bank_date_time");
                            Date date_change = input.parse(date);
                            model.History his = new model.History(object.getString("user_transfer_to_bank_id"), output.format(date_change), "Total Transfer To Bank", time.format(date_change), object.getString("transfered_amount"));
                            list.add(his);
                        }

                        JSONArray friendsTransactions = reader.getJSONArray("completedTransferedToFriendsTransactions");
                        for (int count = 0; count < friendsTransactions.length(); count++) {
                            JSONObject object = friendsTransactions.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("user_transfered_to_friends_date_time");
                            Date date_change = input.parse(date);
                            model.History his = new model.History(object.getString("user_transfered_to_friends_id"), output.format(date_change), "Total Transfer To Friends", time.format(date_change), object.getString("transfered_amount"));
                            list.add(his);
                        }

                        JSONArray donatedTransactions = reader.getJSONArray("completedDonatedTransactions");
                        for (int count = 0; count < donatedTransactions.length(); count++) {
                            JSONObject object = donatedTransactions.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("donated_date_time");
                            Date date_change = input.parse(date);
                            model.History his = new model.History(object.getString("user_donated_id"), output.format(date_change), "Total Donated", time.format(date_change), object.getString("donated_amount"));
                            list.add(his);
                        }


                        history.success(list);
                    } else if (status == 0) {
                        history.error(reader.getString("message"));
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
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

    public void getCanceledHistory() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Get Transaction Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<model.History> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "userHistoryCancelled", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONArray withdrawTransactions = reader.getJSONArray("cancelledWithdrawTransactions");
                        for (int count = 0; count < withdrawTransactions.length(); count++) {
                            JSONObject object = withdrawTransactions.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("withdraw_date_time");
                            Date date_change = input.parse(date);
                            model.History his = new model.History(object.getString("user_withdraw_id"), output.format(date_change), "Total Cash Withdraw", time.format(date_change), object.getString("withdraw_amount"));
                            list.add(his);
                        }

                        JSONArray bankTransactions = reader.getJSONArray("cancelledTransferedToBankTransactions");
                        for (int count = 0; count < bankTransactions.length(); count++) {
                            JSONObject object = bankTransactions.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("user_transfered_to_bank_date_time");
                            Date date_change = input.parse(date);
                            model.History his = new model.History(object.getString("user_transfer_to_bank_id"), output.format(date_change), "Total Transfer To Bank", time.format(date_change), object.getString("transfered_amount"));
                            list.add(his);
                        }

                        JSONArray friendsTransactions = reader.getJSONArray("cancelledTransferedToFriendsTransactions");
                        for (int count = 0; count < friendsTransactions.length(); count++) {
                            JSONObject object = friendsTransactions.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("user_transfered_to_friends_date_time");
                            Date date_change = input.parse(date);
                            model.History his = new model.History(object.getString("user_transfered_to_friends_id"), output.format(date_change), "Total Transfer To Friends", time.format(date_change), object.getString("transfered_amount"));
                            list.add(his);
                        }

                        JSONArray donatedTransactions = reader.getJSONArray("cancelledDonatedTransactions");
                        for (int count = 0; count < donatedTransactions.length(); count++) {
                            JSONObject object = donatedTransactions.getJSONObject(count);
                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("donated_date_time");
                            Date date_change = input.parse(date);
                            model.History his = new model.History(object.getString("user_donated_id"), output.format(date_change), "Total Donated", time.format(date_change), object.getString("donated_amount"));
                            list.add(his);
                        }


                        history.success(list);
                    } else if (status == 0) {
                        history.error(reader.getString("message"));
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
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
