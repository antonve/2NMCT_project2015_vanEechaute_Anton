package be.howest.nmct.project2015;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

    private final static String TAG_FRAGMENT = "MAP";

    public GMapFragment() {

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

            /*

        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_CODE = "code";
        public static final String COLUMN_ADDRESS = "address_text";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LNG = "lng";
        public static final String COLUMN_PHONE_PREFIX = "phone_prefix";
        public static final String COLUMN_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_CAPACITY_AVAILABLE = "capacity_available";
        public static final String COLUMN_CAPACITY_TOTAL = "capacity_total";
        public static final String COLUMN_FULL = "full";
        public static final String COLUMN_OPEN = "open";
             */
            int     id = mCursor.getInt(0),
                    capacity_available = mCursor.getInt(8),
                    capacity_total = mCursor.getInt(9);
            String  description = mCursor.getString(1),
                    code = mCursor.getString(2),
                    address_text = mCursor.getString(3),
                    phone_prefix = mCursor.getString(6),
                    phone_number = mCursor.getString(7);
            boolean full = mCursor.getInt(10) == 1,
                    open = mCursor.getInt(11) == 1;
            double  lat = mCursor.getDouble(4),
                    lng = mCursor.getDouble(5);


            float color = BitmapDescriptorFactory.HUE_GREEN;
            String text = address_text;

            if (full) {
                color = BitmapDescriptorFactory.HUE_RED;
                text += " | Full";
            } else {
                text += " | " + capacity_available + " spots available";
            }

            mMap.addMarker(new MarkerOptions()
                    .title(description)
                    .snippet(text)
                    .position(new LatLng(lat, lng))
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker mark) {
                    /*
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, new DetailFragment(), "")
                            .addToBackStack(TAG_FRAGMENT)
                            .commit();
                            */
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
}
