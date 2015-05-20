package be.howest.nmct.project2015;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

public class GMapFragment extends Fragment {

    private GoogleMap map;

    public GMapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewMap = inflater.inflate(R.layout.fragment_map, container, false);
        initMap();

        return viewMap;
    }

    public void initMap(){
        map = ((MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map)).getMap();
    }
}
