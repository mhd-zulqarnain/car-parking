package zee.example.com.carparking.models;

/**
 * Created by Zul Qarnain on 3/14/2018.
 */

public class ParkPlace {
    String area;
    String alocated;
    String pid;

    public ParkPlace() {
    }

    public ParkPlace(String area, String alocated, String pid) {
        this.area = area;
        this.alocated = alocated;
        this.pid = pid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAlocated() {
        return alocated;
    }

    public void setAlocated(String alocated) {
        this.alocated = alocated;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
