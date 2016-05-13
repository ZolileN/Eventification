package creators.co.eventification.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vidhigoel on 5/8/16.
 */
public class User implements Parcelable {
    private String name;
    private String phone;
    private String reachability;

    public User(String name, String phone, String reachability) {
        this.name = name;
        this.phone = phone;
        this.reachability = reachability;
    }

    public String getReachability() {
        return reachability;
    }

    public void setReachability(String reachability) {
        this.reachability = reachability;
    }

    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(Parcel source)
    {
        name = source.readString();
        phone = source.readString();
        reachability = source.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeString(phone);
        dest.writeString(reachability);
    }
    /**
     * It will be required during un-marshaling data stored in a Parcel
     */
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link android.os.Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
