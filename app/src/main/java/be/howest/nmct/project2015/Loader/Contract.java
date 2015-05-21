package be.howest.nmct.project2015.Loader;

import android.provider.BaseColumns;

public final class Contract {

    public interface ParkingColumns extends BaseColumns {
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
    }
}