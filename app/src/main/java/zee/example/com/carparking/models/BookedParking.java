package zee.example.com.carparking.models;

/**
 * Created by Zul Qarnain on 3/19/2018.
 */

public class BookedParking {
    private String timeIn;
    private String timeOut;
    private String pid;
    private String user;

    public BookedParking() {
    }

    public BookedParking(String timeIn, String timeOut, String pid, String user) {
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.pid = pid;
        this.user = user;
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
}
