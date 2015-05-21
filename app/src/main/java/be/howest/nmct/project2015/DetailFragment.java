package be.howest.nmct.project2015;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DetailFragment extends Fragment {

    private final static String TAG_FRAGMENT = "DETAIL";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewMap = inflater.inflate(R.layout.fragment_detail, container, false);

        return viewMap;
    }
}
