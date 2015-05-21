package be.howest.nmct.project2015;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ParkingLotListFragment extends ListFragment {

    public final static String PARKING_LOTS = "PARKING_LOTS";
    private ArrayList<ParkingLot> mLots;
    private GMapFragment.onChangeFragmentListener changeFragmentListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            changeFragmentListener = (GMapFragment.onChangeFragmentListener) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement ChangeFragmentListener");
        }
    }

    public static ParkingLotListFragment newInstance(Cursor cursor) {
        ParkingLotListFragment fragment = new ParkingLotListFragment();

        Bundle args = new Bundle();
        ArrayList<ParkingLot> lots = new ArrayList<>();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            lots.add(ParkingLot.ParkingLotFromCursor(cursor));
        }

        args.putSerializable(PARKING_LOTS, lots);
        fragment.setArguments(args);

        return fragment;
    }

    class ParkingLotAdapter extends ArrayAdapter<ParkingLot> {
        private ArrayList<ParkingLot> objects;

        public ParkingLotAdapter(Context context, int textViewResourceId, ArrayList<ParkingLot> objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.parking_lot_list_item, null);
            }

            final ParkingLot i = objects.get(position);

            if (i != null) {
                TextView title = ((TextView) view.findViewById(R.id.tvRowTitle));
                title.setText(i.description);

                int color = Color.GREEN;
                if (i.full) {
                    color = Color.RED;
                }

                ((RelativeLayout) title.getParent()).setBackgroundColor(color);

                ((Button) view.findViewById(R.id.btnInfo)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeFragmentListener.onSelectDetail(i);
                    }
                });
            }

            return view;

        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = getListView();
        listView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        mLots = (ArrayList<ParkingLot>) args.getSerializable(PARKING_LOTS);

        setListAdapter(new ParkingLotAdapter(getActivity(), 0, mLots));

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
