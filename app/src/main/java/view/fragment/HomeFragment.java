package view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.johnnylambada.location.LocationObserver;
import com.johnnylambada.location.LocationProvider;
import com.travelcash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import constant.AppData;
import constant.OrderData;
import de.hdodenhof.circleimageview.CircleImageView;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.Vendor;
import presenter.VendorListPresenter;
import view.activity.AddMoney;
import view.activity.ProfileActivity;
import view.activity.ViewVendorReview;
import view.adapter.CashPointAdapter;
import view.customview.CustomButton;
import view.customview.CustomEditText;
import view.customview.CustomTextView;
import view.customview.CustomTextViewBold;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements CashPointAdapter.Clickable, View.OnClickListener, LocationObserver, Runnable, VendorListPresenter.Agent {
    private static HomeFragment fragment;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter cashPointerAdapter;
    private CircleImageView imgProfile;
    private AppCompatImageView imgPlus, imgMinus;
    private AppData appData;
    private View view;
    private LocationProvider locationProvider;
    private CustomTextViewBold tvAmount, tvOne, tvFive, tvTwo, tvWalletBalance, tv_temp, tv_city;
    private CustomEditText edtSearchLocation;
    private VendorListPresenter presenter;
    private CustomButton btnWithPurchase, btnWithPromo, btnTopUp, btnAll;
    private CustomTextView tvLocation;
    private String latitude = "", longitude = "";
    private OrderData orderData;
    private ArrayList<Vendor> response;
    private String apiUrl;

    public static HomeFragment getInstance() {
        fragment = new HomeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        presenter = new VendorListPresenter(getContext(), HomeFragment.this);
        orderData = new OrderData(getContext());
        orderData.clearData();

//        Places.initialize(getContext(), "AIzaSyCVBn21qaBTnSmxNUYDE3obEKqalu2NeEg");
        Places.initialize(getContext(), getString(R.string.google_api_key));

        initView();
        SetPredata();
        return view;
    }

    private void SetPredata() {
        tvWalletBalance.setText(appData.getWalletAmount());
//        if (isNetworkConnected())
//            presenter.getVendorWithPromo(latitude, longitude);
//        else
//            showDialog("Please connect to internet");

    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    private void initView() {
        response = new ArrayList<>();
        edtSearchLocation = view.findViewById(R.id.edtSearchLocation);
        recyclerView = view.findViewById(R.id.recyclerView);
        tvAmount = view.findViewById(R.id.tvAmount);
        imgProfile = view.findViewById(R.id.imgProfile);
        imgPlus = view.findViewById(R.id.imgPlus);
        imgMinus = view.findViewById(R.id.imgMinus);
        tvOne = view.findViewById(R.id.tvOne);
        tvFive = view.findViewById(R.id.tvFive);
        tvTwo = view.findViewById(R.id.tvTwo);
        tvLocation = view.findViewById(R.id.tvLocation);
        btnWithPromo = view.findViewById(R.id.btnWithPromo);
        btnWithPurchase = view.findViewById(R.id.btnWithPurchase);
        tvWalletBalance = view.findViewById(R.id.tvWalletBalance);
        btnTopUp = view.findViewById(R.id.btnTopUp);
        btnAll = view.findViewById(R.id.btnAll);
        tv_temp = view.findViewById(R.id.temp);
        tv_city = view.findViewById(R.id.tv_city);
        edtSearchLocation.setOnClickListener(this);
        imgProfile.setOnClickListener(this);
        imgPlus.setOnClickListener(this);
        imgMinus.setOnClickListener(this);
        tvOne.setOnClickListener(this);
        tvFive.setOnClickListener(this);
        tvTwo.setOnClickListener(this);
        btnWithPromo.setOnClickListener(this);
        btnWithPurchase.setOnClickListener(this);
        btnTopUp.setOnClickListener(this);
        btnAll.setOnClickListener(this);
        appData = new AppData(getContext());
        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(cLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        if (isNetworkConnected()) {
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.persion)
                    .skipMemoryCache(true);

            Glide.with(getActivity()).load(appData.getProfile())
                    .thumbnail(0.5f)
                    .apply(requestOptions)
                    .into(imgProfile);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkLocationPermission()) {
                    LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 101);
                    } else {
                        initLocation();
                    }
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
            } else {
                LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 101);
                } else {
                    initLocation();
                }
            }

        } else {
            showDialog("Please connect to internet");
        }
    }

    private void initLocation() {
        locationProvider = new LocationProvider.Builder(getActivity())
                .locationObserver(this)
                .intervalMs(300000)
                .onPermissionDeniedFirstTime(this)
                .onPermissionDeniedAgain(this)
                .onPermissionDeniedForever(this)
                .build();
        locationProvider.startTrackingLocation();
    }

    private void startAutocompleteActivity() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(getContext());
        startActivityForResult(intent, 103);
    }

    @Override
    public void onClick(int position) {
        if (!tvAmount.getText().toString().equals("0")) {
            orderData.agentID(response.get(position).getVendorID());
            orderData.shopName(response.get(position).getShopName());
            orderData.shopAddress(response.get(position).getShopAddress());
            orderData.shopDistance(response.get(position).getDistance());
            orderData.shopLatitude(response.get(position).getLatitude());
            orderData.shopLongitude(response.get(position).getLongitude());
            orderData.requestAmount(tvAmount.getText().toString().trim());
            String promo = response.get(position).getIsPromo();
            String purchase = response.get(position).getIsPurchase();
            AppData.agentID = response.get(position).getVendorID();
            if (promo.equals("1")) {
                orderData.flagPromos(2);
                orderData.promoDiscount(response.get(position).getPromosDiscount());
            }

            if (purchase.equals("1")) {
                orderData.flagPurchase(1);
                orderData.purchaseAmount(response.get(position).getPurchaseLimit());
            }

            Intent intent = new Intent(getContext(), ViewVendorReview.class);
            intent.putExtra("agentID", response.get(position).getVendorID());
            startActivity(intent);
            Animatoo.animateSlideRight(getContext());
        } else {
            showDialog("Please select amount to withdraw.");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgProfile:
                startActivityForResult(new Intent(getContext(), ProfileActivity.class), 100);
                Animatoo.animateSlideRight(getContext());
                break;

            case R.id.imgMinus:
                minus();
                break;

            case R.id.imgPlus:
//                add();
                Myadd();
                break;

            case R.id.tvOne:
//                add(100000);
                Myadd(100000);

                break;

            case R.id.tvFive:
//                add(500000);
                Myadd(500000);
                break;

            case R.id.tvTwo:
//                add(2000000);
                Myadd(2000000);
                break;

            case R.id.edtSearchLocation:
                locationProvider.stopTrackingLocation();
                startAutocompleteActivity();
                break;

            case R.id.btnWithPromo:
                if (isNetworkConnected())
                    presenter.getVendorWithPromo(latitude, longitude);
                else
                    showDialog("Please connect to internet");
                break;

            case R.id.btnWithPurchase:
                if (isNetworkConnected())
                    presenter.getVendorWithPurchase(latitude, longitude);
                else
                    showDialog("Please connect to internet");
                break;

            case R.id.btnTopUp:
                startActivityForResult(new Intent(getContext(), AddMoney.class), 102);
                Animatoo.animateSlideRight(getContext());
                break;

            case R.id.btnAll:
                if (isNetworkConnected())
                    presenter.getVendor(latitude, longitude);
                else
                    showDialog("Please connect to internet");
                break;
        }
    }




    private void showDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                RequestOptions requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .error(R.drawable.persion)
                        .signature(new ObjectKey(String.valueOf(System.currentTimeMillis())))
                        .skipMemoryCache(true);

                Glide.with(getActivity()).load(appData.getProfile())
                        .thumbnail(0.5f)
                        .apply(requestOptions)
                        .into(imgProfile);
            }
        } else if (requestCode == 101 && resultCode == 0) {
            LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast toast = Toast.makeText(getContext(), "Please turn on GPS.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, 100);
            } else {
                initLocation();
            }
        } else if (requestCode == 102) {
            if (resultCode == RESULT_OK) {
                presenter.getVendor(latitude, longitude);
            }
        } else if (requestCode == 103) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng latLng = place.getLatLng();
                latitude = "" + latLng.latitude;
                longitude = "" + latLng.longitude;
                if (isNetworkConnected()) {
                    presenter.getVendor(latitude, longitude);
                } else {
                    showDialog("Please connect to internet.");
                }
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    Address obj = addresses.get(0);
                    String add = obj.getSubLocality();
                    edtSearchLocation.setText(add);
                    tvLocation.setText(add);
                } catch (IOException e) {
                    e.printStackTrace();
                    showDialog(e.getMessage());
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                showDialog(status.getStatusMessage());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(intent, 101);
                    } else {
                        initLocation();
                    }
                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("")
                            .setMessage("Please allow permission")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                                }
                            }).show();
                }
            }
        }
    }

    @Override
    public void onLocation(Location location) {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getSubLocality();
            String city = addresses.get(0).getLocality();
            tvLocation.setText(address + ", " + city);
            latitude = "" + location.getLatitude();
            longitude = "" + location.getLongitude();

            if (isNetworkConnected()) {
                presenter.getVendor("" + location.getLatitude(), "" + location.getLongitude());
                apiUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&APPID=" + AppData.API_KEY + "&units=metric&sensor=false";
                makeJsonObject(apiUrl);
            } else {
                showDialog("Please connect to internet.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void success(ArrayList<Vendor> response) {
        this.response.clear();
        this.response = response;
        cashPointerAdapter = new CashPointAdapter(getActivity(), response, HomeFragment.this);
        recyclerView.setAdapter(cashPointerAdapter);
//        cashPointerAdapter.notifyDataSetChanged();
        runLayoutAnimation(recyclerView);

    }

    @Override
    public void success(ArrayList<Vendor> response, String amount) {
        this.response.clear();
        this.response = response;
        cashPointerAdapter = new CashPointAdapter(getActivity(), response, HomeFragment.this);
        recyclerView.setAdapter(cashPointerAdapter);

        runLayoutAnimation(recyclerView);

        DecimalFormat df = new DecimalFormat("#,###,###,###");
        double dd = Double.parseDouble(amount);
        tvWalletBalance.setText("" + df.format(dd));//1
    }

    @Override
    public void error(String response) {
//        showDialog(response);
        ShowNewAlert(response);//shyam 26/06/2019
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }

    @Override
    public void onStop() {
        super.onStop();
       /* if (isNetworkConnected())
            locationProvider.stopTrackingLocation();*/
    }

    @Override
    public void onResume() {
        super.onResume();
        orderData.clearData();
        tvAmount.setText("0");
       /* if (isNetworkConnected())
            locationProvider.startTrackingLocation();*/
    }

    public void makeJsonObject(final String apiUrl) {
        Log.e("","apiUrl= "+apiUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject mainObject = jsonObject.getJSONObject("main");
                    String temp = mainObject.getString("temp");
                    Long tempVal = Math.round(Math.floor(Double.parseDouble(temp)));
                    String weatherTemp = String.valueOf(tempVal) + "°C,";
                    tv_temp.setText(weatherTemp);
                    tv_city.setText(jsonObject.getString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
    PrettyDialog prettyDialog=null;
    private void ShowNewAlert(String message) {
        if(prettyDialog!=null)
        {
            prettyDialog.dismiss();
        }
         prettyDialog = new PrettyDialog(getContext());
        prettyDialog.setCanceledOnTouchOutside(false);
        TextView title = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_title);
        TextView tvmessage = (TextView) prettyDialog.findViewById(libs.mjn.prettydialog.R.id.tv_message);
        title.setTextSize(15);
        tvmessage.setTextSize(15);
        prettyDialog.setIconTint(R.color.colorPrimary);
        prettyDialog.setIcon(R.drawable.pdlg_icon_info);
        prettyDialog.setTitle("");
        prettyDialog.setMessage(message);
        prettyDialog.setAnimationEnabled(false);
        prettyDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        prettyDialog.addButton("Cancel", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
            }
        }).show();

        prettyDialog.addButton("Search again", R.color.black, R.color.white, new PrettyDialogCallback() {
            @Override
            public void onClick() {
                prettyDialog.dismiss();
                if (isNetworkConnected()) {
                    presenter.getVendorWithPromo(latitude, longitude);
                }
                else {
                    showDialog("Please connect to internet");
                }
            }
        }).show();
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_bottom);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
    int sAmount=0;
    private void Myadd(int value) {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);
        sAmount=value;
        int val = amt + value;
        DecimalFormat df = new DecimalFormat("#,###,###,###");
        double dd = Double.parseDouble("" + val);
        tvAmount.setText("" + df.format(dd));
    }
    private void Myadd() {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);
       if(sAmount==0)
       {
           sAmount=100000;
       }
        int val = amt + sAmount;
        DecimalFormat df = new DecimalFormat("#,###,###,###");
        double dd = Double.parseDouble("" + val);
        tvAmount.setText("" + df.format(dd));
    }

    private void minus() {
        String amount = tvAmount.getText().toString().trim();
        amount = amount.replaceAll(",", "");
        int amt = Integer.parseInt(amount);

        if (amt > sAmount) {
            int val = amt - sAmount;
            DecimalFormat df = new DecimalFormat("#,###,###,###");
            double dd = Double.parseDouble("" + val);
            tvAmount.setText("" + df.format(dd));
        }
    }

}
