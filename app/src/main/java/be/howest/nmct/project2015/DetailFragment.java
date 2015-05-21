package be.howest.nmct.project2015;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailFragment extends Fragment {

    public final static String PARKING_LOT = "PARKING_LOT";

    public TextView tvTitle;
    public TextView tvAddress;
    public TextView tvAvailability;
    public Button btnCall;

    private GoogleMap mMap;
    private ParkingLot mLot;

    public static DetailFragment newInstance(ParkingLot lot) {
        DetailFragment fragment = new DetailFragment();

        Bundle args = new Bundle();
        args.putSerializable(PARKING_LOT, lot);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        mLot = (ParkingLot) args.getSerializable(PARKING_LOT);

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvTitle.setText(mLot.description);

        tvAddress = (TextView) view.findViewById(R.id.tvAddress);
        tvAddress.setText(mLot.address_text);

        tvAvailability = (TextView) view.findViewById(R.id.tvAvailability);
        String availability;
        if (mLot.full) {
            availability = "No spots available";
            tvAvailability.setBackgroundColor(Color.RED);
        } else {
            availability = "There are " + mLot.capacity_available + " spots available.";
            tvAvailability.setBackgroundColor(Color.GREEN);
        }
        tvAvailability.setText(availability);

        btnCall = (Button) view.findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + mLot.phone_prefix + mLot.phone_number));
                startActivity(callIntent);
            }
        });

        initMap();

        return view;
    }

    public void initMap(){
        mMap = ((MapFragment) getChildFragmentManager().findFragmentById(R.id.mapDetail)).getMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLot.lat, mLot.lng), 14.0f));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);

        float color = BitmapDescriptorFactory.HUE_GREEN;

        if (mLot.full) {
            color = BitmapDescriptorFactory.HUE_RED;
        }

        mMap.addMarker(new MarkerOptions()
                .title(mLot.description)
                .snippet(mLot.address_text)
                .position(new LatLng(mLot.lat, mLot.lng))
                .icon(BitmapDescriptorFactory.defaultMarker(color))
        ).showInfoWindow();

    }

}
