package view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.travelcash.R;

import constant.OrderData;
import view.customview.CustomTextViewBold;

public class PurchaseFragment extends Fragment {
    private CustomTextViewBold tvLabel;
    private OrderData orderData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);

        orderData  = new OrderData(getContext());
        tvLabel = view.findViewById(R.id.tvLabel);
        int flagPurchase = orderData.flagPurchase();
        int flagPromos = orderData.flagPromos();
        if(flagPurchase == 1)
            tvLabel.setText("Minimum purchase required : IDR " + orderData.requestAmount());
        else
            tvLabel.setText("No Purchase Required.");

         if(flagPromos == 2)
            tvLabel.setText("Discount offer : " + orderData.purchaseAmount() + "%");


        return view;
    }
}
