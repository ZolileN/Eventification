package creators.co.eventification.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vidhigoel on 5/8/16.
 */
public class Event implements Parcelable {

    private String id;
    private String name;
    private String location;
    private String startdate;
    private String enddate;


    public Event(String id, String name, String location, String startdate, String enddate) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startdate = startdate;
        this.enddate = enddate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(startdate);
        dest.writeString(enddate);

    }

    public Event(Parcel source)
    {
        id = source.readString();
        name = source.readString();
        location = source.readString();
        startdate = source.readString();
        enddate = source.readString();
    }

    /**
     * It will be required during un-marshaling data stored in a Parcel
     */
    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link android.os.Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}
