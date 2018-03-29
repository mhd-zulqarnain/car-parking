package zee.example.com.carparking.models;

/**
 * Created by Zul Qarnain on 3/14/2018.
 */

public class ParkPlace {
    String area;
    String description;
    String pid;

    public ParkPlace() {
    }

    public ParkPlace(String area, String description, String pid) {
        this.area = area;
        this.description = description;
        this.pid = pid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
