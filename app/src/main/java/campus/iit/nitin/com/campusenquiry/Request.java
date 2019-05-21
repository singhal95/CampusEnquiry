package campus.iit.nitin.com.campusenquiry;

/**
 * Created by nitinsinghal on 21/05/19.
 */

public class Request {

    String name,remaks;

    public Request(String name, String remaks) {
        this.name = name;
        this.remaks = remaks;
    }

    public Request(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRemaks(String remaks) {
        this.remaks = remaks;
    }

    public String getName() {
        return name;
    }

    public String getRemaks() {
        return remaks;
    }
}
