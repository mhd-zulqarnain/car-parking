package zee.example.com.carparking.models;

/**
 * Created by Zul Qarnain on 3/19/2018.
 */

public class Booked {
    private String timeIn;
    private String timeOut;
    private String pid;
    private String user;
    private String area;
    private String bid;

    public Booked() {
    }

    public Booked(String timeIn, String timeOut, String pid, String user, String area, String bid) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.pid = pid;
        this.user = user;
        this.area = area;
        this.bid = bid;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getbid() {
        return bid;
    }

    public void setbid(String bid) {
        this.bid = bid;
    }
}
