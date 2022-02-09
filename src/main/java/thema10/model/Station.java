package thema10.model;

public class Station {

    /* Data example:
        Note: only add the fields required for download
            "elevation": 223.1,
            "mindate": "1931-01-01",
            "maxdate": "1970-12-01",
            "latitude": 32.6,
            "name": "AUBURN, AL US",
            "datacoverage": 0.9917,
            "id": "COOP:010422",
            "elevationUnit": "METERS",
            "longitude": -85.5
     */

    private float elevation;
    private String mindage;
    private String maxdata;
    private float latitude;
    private float longitude;
    private String name;
    private String id;
    private float datacoverage;
    private String elevationUnit;

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public String getMindage() {
        return mindage;
    }

    public void setMindage(String mindage) {
        this.mindage = mindage;
    }

    public String getMaxdata() {
        return maxdata;
    }

    public void setMaxdata(String maxdata) {
        this.maxdata = maxdata;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDatacoverage() {
        return datacoverage;
    }

    public void setDatacoverage(float datacoverage) {
        this.datacoverage = datacoverage;
    }

    public String getElevationUnit() {
        return elevationUnit;
    }

    public void setElevationUnit(String elevationUnit) {
        this.elevationUnit = elevationUnit;
    }

}

