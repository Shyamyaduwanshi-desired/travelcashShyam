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
import model.Vendor;

public class VendorListPresenter {
    private Context context;
    private Agent agent;
    private AppData appData;

    public VendorListPresenter(Context context, Agent agent) {
        this.context = context;
        this.agent = agent;
        appData = new AppData(context);
    }

    public interface Agent {
        void success(ArrayList<Vendor> response);

        void success(ArrayList<Vendor> response, String amount);

        void error(String response);

        void fail(String response);
    }

    public void getVendor(final String latitude, final String longitude) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "agentSortedList", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        ArrayList<Vendor> list = new ArrayList<>();
                        list.clear();
                        JSONArray jsonArray = reader.getJSONArray("agentList");
                        for (int count = 0; count < jsonArray.length(); count++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(count);
                            Vendor vendor = new Vendor();
                            vendor.setVendorID(jsonObject.getString("agent_id"));
                            vendor.setShopName(jsonObject.getString("agent_fname"));
                            vendor.setShopImage(jsonObject.getString("image_url"));
                            vendor.setShopAddress(jsonObject.getString("agent_address"));
                            vendor.setDistance(jsonObject.getString("distance"));
                            vendor.setLatitude(jsonObject.getString("agent_latitude"));
                            vendor.setLongitude(jsonObject.getString("agent_longitude"));
                            vendor.setIsPromo(jsonObject.getString("is_promos"));
                            vendor.setPromosDiscount(jsonObject.getString("promos_discount"));
                            vendor.setIsPurchase(jsonObject.getString("is_purchase"));
                            vendor.setPurchaseLimit(jsonObject.getString("purchase_limit"));
                            list.add(vendor);
                        }

                        agent.success(list, reader.getString("userWallet"));
                    } else if (status == 0) {
                        agent.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    agent.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                agent.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("user_id", appData.getUserID());
                Log.e("","latitude= "+latitude+" longitude= "+longitude+" appData.getUserID()= "+appData.getUserID());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void getVendorWithPromo(final String latitude, final String longitude) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showAgentWithPromos", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        ArrayList<Vendor> list = new ArrayList<>();
                        list.clear();
                        JSONArray jsonArray = reader.getJSONArray("agentList");
                        for (int count = 0; count < jsonArray.length(); count++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(count);
                            Vendor vendor = new Vendor();
                            vendor.setVendorID(jsonObject.getString("agent_id"));
                            vendor.setShopName(jsonObject.getString("agent_fname"));
                            vendor.setShopImage(jsonObject.getString("image_url"));
                            vendor.setShopAddress(jsonObject.getString("agent_address"));
                            vendor.setDistance(jsonObject.getString("distance"));
                            vendor.setLatitude(jsonObject.getString("agent_latitude"));
                            vendor.setLongitude(jsonObject.getString("agent_longitude"));
                            vendor.setIsPromo(jsonObject.getString("is_promos"));
                            vendor.setPromosDiscount(jsonObject.getString("promos_discount"));
                            vendor.setIsPurchase(jsonObject.getString("is_purchase"));
                            vendor.setPurchaseLimit(jsonObject.getString("purchase_limit"));
                            list.add(vendor);
                        }

                        agent.success(list);
                    } else if (status == 0) {
                        agent.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    agent.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                agent.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("is_promos", "1");
                params.put("user_id", appData.getUserID());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }

    public void getVendorWithPurchase(final String latitude, final String longitude) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, AppData.url + "showAgentWithPurchase", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                try {
                    JSONObject reader = new JSONObject(response);
                    int status = reader.getInt("status");
                    if (status == 1) {
                        ArrayList<Vendor> list = new ArrayList<>();
                        list.clear();
                        JSONArray jsonArray = reader.getJSONArray("agentList");
                        for (int count = 0; count < jsonArray.length(); count++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(count);
                            Vendor vendor = new Vendor();
                            vendor.setVendorID(jsonObject.getString("agent_id"));
                            vendor.setShopName(jsonObject.getString("agent_fname"));
                            vendor.setShopImage(jsonObject.getString("image_url"));
                            vendor.setShopAddress(jsonObject.getString("agent_address"));
                            vendor.setDistance(jsonObject.getString("distance"));
                            vendor.setLatitude(jsonObject.getString("agent_latitude"));
                            vendor.setLongitude(jsonObject.getString("agent_longitude"));
                            vendor.setIsPromo(jsonObject.getString("is_promos"));
                            vendor.setPromosDiscount(jsonObject.getString("promos_discount"));
                            vendor.setIsPurchase(jsonObject.getString("is_purchase"));
                            vendor.setPurchaseLimit(jsonObject.getString("purchase_limit"));
                            list.add(vendor);
                        }

                        agent.success(list);
                    } else if (status == 0) {
                        agent.error(reader.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    agent.fail("Something went wrong. Please try after some time.");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                agent.fail("Server Error.\n Please try after some time.");
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("is_purchase", "1");
                params.put("user_id", appData.getUserID());

                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
    }
}
