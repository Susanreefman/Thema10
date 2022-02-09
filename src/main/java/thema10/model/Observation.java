package thema10.model;

import com.google.gson.annotations.SerializedName;

public class Observation {

    @SerializedName("eventDate")
    private String date;

    private String key;
    private String country;

    @SerializedName("identificationVerificationStatus")
    private String verification;

    @SerializedName("decimalLatitude")
    private float lat;
    @SerializedName("decimalLongitude")
    private float lon;

    @Override
    public String toString() {
        return "Observation{" +
                "date='" + date + '\'' +
                ", key='" + key + '\'' +
                ", country='" + country + '\'' +
                ", verification='" + verification + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
