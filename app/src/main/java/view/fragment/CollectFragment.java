package view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.travelcash.R;

import constant.OrderData;
import view.customview.CustomTextView;
import view.customview.CustomTextViewBold;

public class CollectFragment extends Fragment {
    private CustomTextView tvDirection, tvDistance, tvAddress;
    private CustomTextViewBold tvShopName, tvAmount;
    private OrderData orderData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collectby, container, false);

        orderData = new OrderData(getContext());
        tvShopName = view.findViewById(R.id.tvShopName);
        tvAddress = view.findViewById(R.id.tvAddress);
        tvAmount = view.findViewById(R.id.tvAmount);
        tvDistance = view.findViewById(R.id.tvDistance);
        tvDirection = view.findViewById(R.id.tvDirection);

        tvShopName.setText(orderData.shopName());
        tvAddress.setText(orderData.shopAddress());
        tvDistance.setText(orderData.shopDistance() + "KM away");
        tvAmount.setText("IDR " + orderData.requestAmount());

        tvDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String latitude  = orderData.shopLatitude();
                String longitude  = orderData.shopLongitude();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr=" + latitude + "," + longitude));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}
