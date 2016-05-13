package creators.co.eventification.model;

import android.location.Location;

/**
 * Created by vidhigoel on 5/10/16.
 */
public class UserLocation {
    private Location lastLocation;
    String lastUpdateTime;

    public UserLocation(Location lastLocation, String lastUpdateTime) {
        this.lastLocation = lastLocation;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
