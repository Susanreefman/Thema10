package thema10.model;

public class Species {


    /* Data example:
        Note: only add the fields required for download
        "eventDate": "2020-04-29T06:10:00",
        "key": "2802633944",
        "country": "Peru",
        "identificationVerificationStatus": null,
        "decimalLatitude": -12.298866,
        "decimalLongitude": -68.89192
     */

    private float decimalLatitude;
    private float decimalLongitude;
    private String country;
    private String id;

    public float getDecimalLatitude() {
        return decimalLatitude;
    }
    public void setLatitude(float latitude) {
        this.decimalLatitude = decimalLatitude;
    }

    public float getDecimalLongitude() {
        return decimalLongitude;
    }
    public void setLongitude(float longitude) {
        this.decimalLongitude = decimalLongitude;
    }

    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }

}
