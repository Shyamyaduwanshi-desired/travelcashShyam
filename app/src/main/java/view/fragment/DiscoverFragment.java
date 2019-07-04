package view.fragment;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.johnnylambada.location.LocationObserver;
import com.johnnylambada.location.LocationProvider;
import com.travelcash.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import model.Vendor;
import presenter.VendorListPresenter;
import view.activity.ViewVendorReview;
import view.adapter.CashPointAdapter;
import view.customview.CustomButton;
import view.customview.CustomEditText;
import view.customview.CustomTextView;

import static android.app.Activity.RESULT_OK;

public class DiscoverFragment extends Fragment implements CashPointAdapter.Clickable, LocationObserver, Runnable, VendorListPresenter.Agent, View.OnClickListener {
    private static DiscoverFragment discoverFragment;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter cashPointerAdapter;
    private View view;
    private LocationProvider locationProvider;
    private CustomEditText edtSearchLocation;
    private VendorListPresenter presenter;
    private CustomTextView tvLocation;
    private CustomButton btnWithPurchase, btnWithPromo, btnAll;
    private String latitude = "", longitude = "";
    private ArrayList<Vendor> response;
    PlacesClient placesClient;

    public static DiscoverFragment getInstance() {
        discoverFragment = new DiscoverFragment();
        return discoverFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discover, container, false);

//        Places.initialize(getContext(), "AIzaSyCVBn21qaBTnSmxNUYDE3obEKqalu2NeEg");
//        Places.initialize(getContext(), getString(R.string.google_api_key));
        Places.initialize(getActivity(),getString(R.string.google_api_key));
        placesClient = Places.createClient(getActivity());
        initView();

        return view;
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
        presenter = new VendorListPresenter(getContext(), DiscoverFragment.this);
        tvLocation = view.findViewById(R.id.tvLocation);
        edtSearchLocation = view.findViewById(R.id.edtSearchLocation);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnWithPurchase = view.findViewById(R.id.btnWithPurchase);
        btnWithPromo = view.findViewById(R.id.btnWithPromo);
        btnAll = view.findViewById(R.id.btnAll);
        edtSearchLocation.setOnClickListener(this);
        btnWithPurchase.setOnClickListener(this);
        btnWithPromo.setOnClickListener(this);
        btnAll.setOnClickListener(this);

        RecyclerView.LayoutManager cLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(cLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        if (isNetworkConnected()) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkLocationPermission()) {
                    LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
                .intervalMs(300000)
                .locationObserver(this)
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
        Intent intent = new Intent(getContext(), ViewVendorReview.class);
        intent.putExtra("flagPurchase", "dis");
        intent.putExtra("agentID", response.get(position).getVendorID());
        startActivity(intent);
        Animatoo.animateSlideRight(getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && resultCode == 0) {
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
        } else if (requestCode == 103) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                String addressname="";
                if(TextUtils.isEmpty(place.getAddress()))
                {
                    addressname=place.getName();
                }
                else
                {
                    addressname=place.getAddress()+","+place.getName();
                }
//                String addressname=place.getAddress()+","+place.getName();
                edtSearchLocation.setText(addressname);
                tvLocation.setText(addressname);

                LatLng latLng = place.getLatLng();
                latitude = "" + latLng.latitude;
                longitude = "" + latLng.longitude;
                if (isNetworkConnected()) {
                    presenter.getVendor(latitude, longitude);
                } else {
                    showDialog("Please connect to internet.");
                }
//                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
//                try {
//                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//                    Address obj = addresses.get(0);
//                    String add = obj.getSubLocality();
//                    edtSearchLocation.setText(add);
//                    tvLocation.setText(add);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    showDialog(e.getMessage());
//                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
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
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
            tvLocation.setText(address + " " + city);
            latitude = "" + location.getLatitude();
            longitude = "" + location.getLongitude();

            if (isNetworkConnected()) {
                presenter.getVendor("" + location.getLatitude(), "" + location.getLongitude());
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
        cashPointerAdapter = new CashPointAdapter(getActivity(), response, DiscoverFragment.this);
        recyclerView.setAdapter(cashPointerAdapter);
    }

    @Override
    public void success(ArrayList<Vendor> response, String amount) {
        this.response.clear();
        this.response = response;
        cashPointerAdapter = new CashPointAdapter(getActivity(), response, DiscoverFragment.this);
        recyclerView.setAdapter(cashPointerAdapter);
    }
//search success
    @Override
    public void error(String response)
    {
//        showDialog(response);
        ShowNewAlert(response);//shyam 26/06/2019
    }

    @Override
    public void fail(String response) {
        showDialog(response);
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle("")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isNetworkConnected())
            locationProvider.stopTrackingLocation();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (isNetworkConnected())
            locationProvider.startTrackingLocation();*/
    }

    @Override
    public void onClick(View v) {
        if (v == edtSearchLocation) {
            locationProvider.stopTrackingLocation();
            startAutocompleteActivity();
        } else if (v == btnWithPromo) {
            if (isNetworkConnected())
                presenter.getVendorWithPromo(latitude, longitude);
            else
                showDialog("Please connect to internet");
        } else if (v == btnWithPurchase) {
            if (isNetworkConnected())
                presenter.getVendorWithPurchase(latitude, longitude);
            else
                showDialog("Please connect to internet");
        } else {
            if (isNetworkConnected())
                presenter.getVendor(latitude, longitude);
            else
                showDialog("Please connect to internet");
        }
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
                locationProvider.stopTrackingLocation();
                startAutocompleteActivity();


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
}
