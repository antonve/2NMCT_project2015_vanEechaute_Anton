package be.howest.nmct.project2015;

import android.database.Cursor;
import java.io.Serializable;

public class ParkingLot implements Serializable {
    private static final long serialVersionUID = -2163051469151804394L;

    public int     id = 0,
            capacity_available = 0,
            capacity_total = 0;
    public String  description = "",
            code = "",
            address_text = "",
            phone_prefix = "",
            phone_number = "";
    public boolean full = false,
            open = false;
    public double  lat = 0.0,
            lng = 0.0;

    public ParkingLot()
    {

    }

    public ParkingLot(int id, int capacity_available, int capacity_total, String description, String code, String address_text, String phone_prefix, String phone_number, boolean full, boolean open, double lat, double lng) {
        this.id = id;
        this.capacity_available = capacity_available;
        this.capacity_total = capacity_total;
        this.description = description;
        this.code = code;
        this.address_text = address_text;
        this.phone_prefix = phone_prefix;
        this.phone_number = phone_number;
        this.full = full;
        this.open = open;
        this.lat = lat;
        this.lng = lng;
    }

    public static ParkingLot ParkingLotFromCursor(Cursor cursor) {
        ParkingLot obj = new ParkingLot();

        obj.id = cursor.getInt(0);
        obj.capacity_available = cursor.getInt(8);
        obj.capacity_total = cursor.getInt(9);
        obj.description = cursor.getString(1);
        obj.code = cursor.getString(2);
        obj.address_text = cursor.getString(3);
        obj.phone_prefix = cursor.getString(6);
        obj.phone_number = cursor.getString(7);
        obj.full = cursor.getInt(10) == 1;
        obj.open = cursor.getInt(11) == 1;
        obj.lat = cursor.getDouble(4);
        obj.lng = cursor.getDouble(5);

        return obj;
    }
}
