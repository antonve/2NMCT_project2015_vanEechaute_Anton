package be.howest.nmct.project2015;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import be.howest.nmct.project2015.Loader.ParkingLoader;

public class GMapFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GoogleMap mMap;
    private Cursor mCursor;
    private onChangeFragmentListener changeFragmentListener;

    private final static String TAG_FRAGMENT = "MAP";

    public GMapFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            changeFragmentListener = (onChangeFragmentListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ChangeFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewMap = inflater.inflate(R.layout.fragment_map, container, false);
        initMap();

        return viewMap;
    }

    public void initMap(){
        mMap = ((MapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.05346, 3.7303799999999682), 14.0f));

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
    }

    private void loadMarkers()
    {
        mMap.clear();

        if (mCursor == null) return;

        for (int i = 0; i < mCursor.getCount(); i ++) {
            mCursor.moveToPosition(i);

            final ParkingLot parkingLot = ParkingLot.ParkingLotFromCursor(mCursor);


            float color = BitmapDescriptorFactory.HUE_GREEN;
            String text = parkingLot.address_text;

            if (parkingLot.full) {
                color = BitmapDescriptorFactory.HUE_RED;
                text += " | Full";
            } else {
                text += " | " + parkingLot.capacity_available + " spots available";
            }

            mMap.addMarker(new MarkerOptions()
                    .title(parkingLot.description)
                    .snippet(text)
                    .position(new LatLng(parkingLot.lat, parkingLot.lng))
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker mark) {
                    changeFragmentListener.onSelectDetail(parkingLot);
                }
            });
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Start Loader
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new ParkingLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mCursor = cursor;

        loadMarkers();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    // communicate with other fragments
    public interface onChangeFragmentListener {
        void onSelectDetail(ParkingLot lot);
    }
}
