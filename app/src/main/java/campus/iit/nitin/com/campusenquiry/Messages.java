package campus.iit.nitin.com.campusenquiry;

/**
 * Created by nitinsinghal on 22/05/19.
 */

public class Messages {
    String name,message;

    public Messages(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public Messages() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }
}
