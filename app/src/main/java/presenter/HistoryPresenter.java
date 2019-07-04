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
import model.HistoryModel;

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
        void success(ArrayList<HistoryModel> response);

        void error(String response);

        void fail(String response);
    }

    public void getCompletedHistory() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Get Transaction Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<HistoryModel> list = new ArrayList<>();

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
//                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
//                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("withdraw_date_time");
//                            Date date_change = input.parse(date);
                            HistoryModel his = new HistoryModel(object.getString("user_withdraw_id"),date, "Total Cash Withdraw", date, object.getString("withdraw_amount"),"");
//                            HistoryModel his = new HistoryModel(object.getString("user_withdraw_id"),output.format(date_change), "Total Cash Withdraw", time.format(date_change), object.getString("withdraw_amount"));
                            list.add(his);
                        }

                        JSONArray bankTransactions = reader.getJSONArray("completedTransferedToBankTransactions");
                        for (int count = 0; count < bankTransactions.length(); count++) {
                            JSONObject object = bankTransactions.getJSONObject(count);
//                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
//                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("user_transfered_to_bank_date_time");
//                            Date date_change = input.parse(date);
//                            HistoryModel his = new HistoryModel(object.getString("user_transfer_to_bank_id"), output.format(date_change), "Total Transfer To Bank", time.format(date_change), object.getString("transfered_amount"));
//                            create new object only for show information with proper date
                            HistoryModel his = new HistoryModel(object.getString("user_transfer_to_bank_id"), date, "Total Transfer To Bank", date, object.getString("transfered_amount"),"");
                            list.add(his);
                        }

                        JSONArray friendsTransactions = reader.getJSONArray("completedTransferedToFriendsTransactions");
                        for (int count = 0; count < friendsTransactions.length(); count++) {
                            JSONObject object = friendsTransactions.getJSONObject(count);
//                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
//                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("user_transfered_to_friends_date_time");
//                            Date date_change = input.parse(date);
//                            HistoryModel his = new HistoryModel(object.getString("user_transfered_to_friends_id"), output.format(date_change), "Total Transfer To Friends", time.format(date_change), object.getString("transfered_amount"));

                            HistoryModel his = new HistoryModel(object.getString("user_transfered_to_friends_id"), date, "Total Transfer To Friends", date, object.getString("transfered_amount"),"");
                            list.add(his);
                        }

                        JSONArray donatedTransactions = reader.getJSONArray("completedDonatedTransactions");
                        for (int count = 0; count < donatedTransactions.length(); count++) {
                            JSONObject object = donatedTransactions.getJSONObject(count);
//                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
//                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String date = object.getString("donated_date_time");
//                            Date date_change = input.parse(date);
//                            HistoryModel his = new HistoryModel(object.getString("user_donated_id"), output.format(date_change), "Total Donated", time.format(date_change), object.getString("donated_amount"));

                            HistoryModel his = new HistoryModel(object.getString("user_donated_id"), date, "Total Donated", date, object.getString("donated_amount"),"");
                            list.add(his);
                        }

                        JSONArray receiveFromFriend = reader.getJSONArray("receiveFromFriend");
                        for (int count = 0; count < receiveFromFriend.length(); count++) {
                            JSONObject object = receiveFromFriend.getJSONObject(count);
//                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
//                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");

                            String date = object.getString("user_transfered_to_friends_date_time");

//                            Date date_change = input.parse(date);
                            //object.getString("user_transfered_to_friends_id")
                            HistoryModel his = new HistoryModel("",date, object.getString("user_fname"), date, object.getString("transfered_amount"),"");
                            list.add(his);
                        }





                        history.success(list);
                    } else if (status == 0) {
                        history.error(reader.getString("message"));
                    }
                } /*catch (ParseException ex) {
                    ex.printStackTrace();
                } */catch (JSONException e) {
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

    public void getOngoingHistory() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Get Transaction Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<HistoryModel> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "getUserInProgessRequests", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONArray withdrawTransactions = reader.getJSONArray("userInProgessRequest");
                        for (int count = 0; count < withdrawTransactions.length(); count++) {
                            JSONObject object = withdrawTransactions.getJSONObject(count);

//                            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                            SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
//                            SimpleDateFormat time = new SimpleDateFormat("hh:mm");
                            String agent_recieved_request_id = object.getString("agent_recieved_request_id");

                            String status1 = object.getString("status");
                            String date = object.getString("request_date");
                            String agentid = object.getString("user_request_agent_id");
//                            Date date_change = input.parse(date);


                            HistoryModel his = new HistoryModel(agent_recieved_request_id,date, object.getString("agent_fname"), status1, object.getString("user_request_amount"),agentid);
//                            String id, String date, String mode, String time, String amount
                            list.add(his);
                        }

                        JSONArray userpendingReq = reader.getJSONArray("userPendingRequest");
                        for (int count = 0; count < userpendingReq.length(); count++) {
                            JSONObject object = userpendingReq.getJSONObject(count);

                            String agent_recieved_request_id = object.getString("agent_recieved_request_id");

                            String status1 = object.getString("status");
                            String date = object.getString("request_date");
//                            Date date_change = input.parse(date);


                            HistoryModel his = new HistoryModel(agent_recieved_request_id,date, object.getString("agent_fname"), status1, object.getString("user_request_amount"),"");
//                            String id, String date, String mode, String time, String amount
                            list.add(his);
                        }

                        history.success(list);
                    } else if (status == 0) {
                        history.error(reader.getString("message"));
                    }
                }catch (Exception e) {
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
        final ArrayList<HistoryModel> list = new ArrayList<>();

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
                            HistoryModel his = new HistoryModel(object.getString("user_withdraw_id"), output.format(date_change), "Total Cash Withdraw", time.format(date_change), object.getString("withdraw_amount"),"");
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
                            HistoryModel his = new HistoryModel(object.getString("user_transfer_to_bank_id"), output.format(date_change), "Total Transfer To Bank", time.format(date_change), object.getString("transfered_amount"),"");
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
                            HistoryModel his = new HistoryModel(object.getString("user_transfered_to_friends_id"), output.format(date_change), "Total Transfer To Friends", time.format(date_change), object.getString("transfered_amount"),"");
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
                            HistoryModel his = new HistoryModel(object.getString("user_donated_id"), output.format(date_change), "Total Donated", time.format(date_change), object.getString("donated_amount"),"");
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

    public void getCanceledHistoryNew() {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Get Transaction Please Wait..");
        progress.setCancelable(false);
        progress.show();
        final ArrayList<HistoryModel> list = new ArrayList<>();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "getCancelledAndRejectedRequest", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        list.clear();
                        JSONArray withdrawTransactions = reader.getJSONArray("result");
                        for (int count = 0; count < withdrawTransactions.length(); count++) {
                            JSONObject object = withdrawTransactions.getJSONObject(count);
                            String transaction_id = object.getString("transaction_id");
                            String request_date = object.getString("request_date");
                            String status1 = object.getString("status");
                            String user_request_amount = object.getString("user_request_amount");
                            String agent_recieved_request_id = object.getString("agent_recieved_request_id");
                            String request_updated_on = object.getString("request_updated_on");

                            HistoryModel his = new HistoryModel(transaction_id,request_date, status1, request_updated_on/*agent_recieved_request_id*/, user_request_amount,"");
//                            String id, String date, String mode, String time, String amount
                            list.add(his);
                        }

                        history.success(list);
                    } else if (status == 0) {
                        history.error(reader.getString("message"));
                    }
                }  catch (JSONException e) {
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
