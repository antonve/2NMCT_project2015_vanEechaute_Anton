package be.howest.nmct.project2015;

import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity implements GMapFragment.onChangeFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new GMapFragment())
                    .commit();
        }
    }

    @Override
    public void onSelectDetail(ParkingLot lot) {
        showFragmentDetail(lot);
    }

    private void showFragmentDetail(ParkingLot lot) {
        DetailFragment fragment = DetailFragment.newInstance(lot);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("MAP")
                .commit();
    }
}
