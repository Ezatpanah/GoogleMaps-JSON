

 package ir.rojadev.maps.Model;

import java.io.Serializable;

public class Map_info implements Serializable
{
    private String name;
    private double lat_json;
    private double lng_json;
    private String tell;
    private String adrs;
    private String pic;


    public Map_info(String name, double lat_json, double lng_json, String tell, String adrs, String pic) {
        this.name = name;
        this.lat_json = lat_json;
        this.lng_json = lng_json;
        this.tell = tell;
        this.adrs = adrs;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat_json() {
        return lat_json;
    }

    public void setLat_json(double lat_json) {
        this.lat_json = lat_json;
    }

    public double getLng_json() {
        return lng_json;
    }

    public void setLng_json(double lng_json) {
        this.lng_json = lng_json;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getAdrs() {
        return adrs;
    }

    public void setAdrs(String adrs) {
        this.adrs = adrs;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}