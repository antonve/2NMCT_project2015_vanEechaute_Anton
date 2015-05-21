package be.howest.nmct.project2015;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity implements GMapFragment.onChangeFragmentListener, FragmentManager.OnBackStackChangedListener {

    private boolean isFinishedLoading = false;
    private boolean isListShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new GMapFragment())
                    .commit();
        }

        shouldDisplayHomeUp();
    }

    @Override
    public void onSelectDetail(ParkingLot lot) {
        showFragmentDetail(lot);
    }

    @Override
    public void onFinishLoading() {
        isFinishedLoading = true;
        invalidateOptionsMenu();
    }

    private void showFragmentDetail(ParkingLot lot) {
        DetailFragment fragment = DetailFragment.newInstance(lot);

        isListShown = true;
        invalidateOptionsMenu();

        getFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showFragmentList() {
        GMapFragment gmap = ((GMapFragment) getFragmentManager().findFragmentById(R.id.container));
        ParkingLotListFragment fragment = ParkingLotListFragment.newInstance(gmap.getParkingLotData());
        isListShown = true;
        invalidateOptionsMenu();

        getFragmentManager().beginTransaction()
                .add(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        boolean canBack = getFragmentManager().getBackStackEntryCount() > 0;

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(canBack);
        }
    }

    @Override
    public boolean onNavigateUp() {
        getFragmentManager().popBackStack();

        if (getFragmentManager().getBackStackEntryCount() == 1) {
            isListShown = false;
            invalidateOptionsMenu();
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        menu.findItem(R.id.view_list).setEnabled(isFinishedLoading & !isListShown);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.view_list) {
            showFragmentList();
        }

        return super.onOptionsItemSelected(item);
    }
}
