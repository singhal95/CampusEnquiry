package campus.iit.nitin.com.campusenquiry;

/**
 * Created by nitinsinghal on 21/05/19.
 */

public class StudentRequest {
   String name,remaks;

    public StudentRequest(String name, String remaks) {
        this.name = name;
        this.remaks = remaks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRemaks(String remaks) {
        this.remaks = remaks;
    }

    public StudentRequest() {
    }

    public String getName() {
        return name;
    }

    public String getRemaks() {
        return remaks;
    }
}
