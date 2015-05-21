package be.howest.nmct.project2015.Loader;


import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ParkingLoader extends AsyncTaskLoader<Cursor> {

    public static Cursor mCursor;

    // clean api for ghent parking data
    private String url = "https://parking-ghent.slashdot.io/api/parking";

    private String[] arrColumnNames = new String[] {
            BaseColumns._ID,
            Contract.ParkingColumns.COLUMN_DESCRIPTION,
            Contract.ParkingColumns.COLUMN_CODE,
            Contract.ParkingColumns.COLUMN_ADDRESS,
            Contract.ParkingColumns.COLUMN_LAT,
            Contract.ParkingColumns.COLUMN_LNG,
            Contract.ParkingColumns.COLUMN_PHONE_PREFIX,
            Contract.ParkingColumns.COLUMN_PHONE_NUMBER,
            Contract.ParkingColumns.COLUMN_CAPACITY_AVAILABLE,
            Contract.ParkingColumns.COLUMN_CAPACITY_TOTAL,
            Contract.ParkingColumns.COLUMN_FULL,
            Contract.ParkingColumns.COLUMN_OPEN
    };

    private Object lock = new Object();

    public ParkingLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null) {
            deliverResult(mCursor);
        }
        if (takeContentChanged() || mCursor == null) {
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        if (mCursor == null) {
            loadCursor();
        }
        return mCursor;
    }

    private void loadCursor() {

        synchronized (lock) {

            // new matrix cursor from column names
            MatrixCursor cursor = new MatrixCursor(arrColumnNames);
            InputStream input;
            JsonReader reader;

            // retrieve json feed (don't forget to set permissions)
            try {
                input = new URL(url).openStream();
                reader = new JsonReader(new InputStreamReader(input, "UTF-8"));


                reader.beginObject();

                while (reader.hasNext()) {
                    String name = reader.nextName();

                    // only use the data array
                    if (name.equals("data")) {
                        reader.beginArray();

                        // loop every parking lot in the json feed and fill in the cursor
                        while (reader.hasNext()) {
                            reader.beginObject();

                            int id = 0,
                                    capacity_available = 0,
                                    capacity_total = 0;
                            String description = "",
                                    code = "",
                                    address_text = "",
                                    phone_prefix = "",
                                    phone_number = "";
                            boolean full = false,
                                    open = false;
                            double lat = 0.0,
                                    lng = 0.0;

                            // loop parking lot properties
                            while (reader.hasNext()) {
                                String key = reader.nextName();

                                if (key.equals("id")) {
                                    id = reader.nextInt();
                                } else if (key.equals("capacity_available")) {
                                    capacity_available = reader.nextInt();
                                } else if (key.equals("capacity_total")) {
                                    capacity_total = reader.nextInt();
                                } else if (key.equals("description")) {
                                    description = reader.nextString();
                                } else if (key.equals("code")) {
                                    code = reader.nextString();
                                } else if (key.equals("address_text")) {
                                    address_text = reader.nextString();
                                } else if (key.equals("phone_prefix")) {
                                    phone_prefix = reader.nextString();
                                } else if (key.equals("phone_number")) {
                                    phone_number = reader.nextString();
                                } else if (key.equals("full")) {
                                    full = reader.nextBoolean();
                                } else if (key.equals("open")) {
                                    open = reader.nextBoolean();
                                } else if (key.equals("lat")) {
                                    lat = reader.nextDouble();
                                } else if (key.equals("lng")) {
                                    lng = reader.nextDouble();
                                } else {
                                    reader.skipValue();
                                }
                            }

                            MatrixCursor.RowBuilder row = cursor.newRow();
                            row.add(id);
                            row.add(description);
                            row.add(code);
                            row.add(address_text);
                            row.add(lat);
                            row.add(lng);
                            row.add(phone_prefix);
                            row.add(phone_number);
                            row.add(capacity_available);
                            row.add(capacity_total);
                            row.add(full);
                            row.add(open);

                            reader.endObject();
                        }

                        reader.endArray();
                    } else {
                        reader.skipValue();
                    }
                }

                reader.endObject();
                mCursor = cursor;
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
