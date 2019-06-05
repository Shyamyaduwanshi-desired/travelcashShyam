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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import constant.AppData;
import model.TransactionModel;

public class TransactionDetailPresenter {
    private Context context;
    private Transaction transaction;
    private AppData appData;

    public TransactionDetailPresenter(Context context, Transaction transaction) {
        this.context = context;
        this.transaction = transaction;
        appData = new AppData(context);
    }

    public interface Transaction {
        void success(TransactionModel response);

        void error(String response);

        void fail(String response);
    }

    public void showWithdrawTransactionInformation(final String transactionId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showWithdrawTransactionInformation", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        TransactionModel model = new TransactionModel();
                        JSONObject jsonObject = reader.getJSONObject("withdrawTransactionData");
                        model.setTransactionID(jsonObject.getString("transaction_id"));
                        model.setBankTransactionID(jsonObject.getString("bank_transaction_id"));
                        model.setRefundID(jsonObject.getString("refund_id"));
                        model.setRequestAmount(jsonObject.getString("withdraw_amount"));
                        model.setDebitAmount(jsonObject.getString("withdraw_amount"));
                        model.setAgentID(jsonObject.getString("agent_id"));
                        model.setAddress(jsonObject.getString("agent_address"));
                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, hh:mm");
                        String date = jsonObject.getString("withdraw_date_time");
                        Date date_change = input.parse(date);
                        model.setDate(output.format(date_change));
                        transaction.success(model);
                    } else if (status == 0) {
                        transaction.error(reader.getString("message"));
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    transaction.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                transaction.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("withdrawTransactionId", transactionId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void showTransferToFriendTransaction(final String transactionId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showTranseferToFriendTransactionInformation", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        TransactionModel model = new TransactionModel();
                        JSONObject jsonObject = reader.getJSONObject("transeferToFriendTransactionData");
                        model.setTransactionID(jsonObject.getString("transaction_id"));
                        model.setBankTransactionID(jsonObject.getString("bank_transaction_id"));
                        model.setRefundID(jsonObject.getString("refund_id"));
                        model.setRequestAmount(jsonObject.getString("transfered_amount"));
                        model.setDebitAmount(jsonObject.getString("transfered_amount"));
                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, hh:mm");
                        String date = jsonObject.getString("user_transfered_to_friends_date_time");
                        Date date_change = input.parse(date);
                        model.setDate(output.format(date_change));
                        transaction.success(model);
                    } else if (status == 0) {
                        transaction.error(reader.getString("message"));
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    transaction.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                transaction.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("transeferToFriendTransactionId", transactionId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void showTransferToBankTransaction(final String transactionId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showTranseferToBankTransactionInformation", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        TransactionModel model = new TransactionModel();
                        JSONObject jsonObject = reader.getJSONObject("transeferToBankTransactionData");
                        model.setTransactionID(jsonObject.getString("transaction_id"));
                        model.setBankTransactionID(jsonObject.getString("bank_transaction_id"));
                        model.setRefundID(jsonObject.getString("refund_id"));
                        model.setRequestAmount(jsonObject.getString("transfered_amount"));
                        model.setDebitAmount(jsonObject.getString("transfered_amount"));
                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, hh:mm");
                        String date = jsonObject.getString("user_transfered_to_bank_date_time");
                        Date date_change = input.parse(date);
                        model.setDate(output.format(date_change));
                        transaction.success(model);
                    } else if (status == 0) {
                        transaction.error(reader.getString("message"));
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    transaction.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                transaction.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("transeferToBankTransactionId", transactionId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void showDonatedTransaction(final String transactionId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showDonatedTransactionInformation", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        TransactionModel model = new TransactionModel();
                        JSONObject jsonObject = reader.getJSONObject("donatedTransactionData");
                        model.setTransactionID(jsonObject.getString("transaction_id"));
                        model.setBankTransactionID(jsonObject.getString("bank_transaction_id"));
                        model.setRefundID(jsonObject.getString("refund_id"));
                        model.setRequestAmount(jsonObject.getString("donated_amount"));
                        model.setDebitAmount(jsonObject.getString("donated_amount"));
                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, hh:mm");
                        String date = jsonObject.getString("donated_date_time");
                        Date date_change = input.parse(date);
                        model.setDate(output.format(date_change));
                        transaction.success(model);
                    } else if (status == 0) {
                        transaction.error(reader.getString("message"));
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    transaction.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                transaction.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("donatedTransactionId", transactionId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void showWalletTransaction(final String transactionId) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showWalletTransactionInformation", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        TransactionModel model = new TransactionModel();
                        JSONObject jsonObject = reader.getJSONObject("walletTransactionData");
                        model.setTransactionID(jsonObject.getString("transaction_id"));
                        model.setBankTransactionID(jsonObject.getString("bank_transaction_id"));
                        model.setRefundID(jsonObject.getString("refund_id"));
                        model.setRequestAmount(jsonObject.getString("request_amount"));
                        model.setDebitAmount(jsonObject.getString("request_amount"));
                        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy, hh:mm");
                        String date = jsonObject.getString("user_exchange_request_date_time");
                        Date date_change = input.parse(date);
                        model.setDate(output.format(date_change));
                        transaction.success(model);
                    } else if (status == 0) {
                        transaction.error(reader.getString("message"));
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                    transaction.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                transaction.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", appData.getUserID());
                params.put("walletTransactionId", transactionId);

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
