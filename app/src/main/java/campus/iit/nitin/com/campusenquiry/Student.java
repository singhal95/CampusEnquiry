package campus.iit.nitin.com.campusenquiry;

/**
 * Created by nitinsinghal on 21/05/19.
 */

public class Student {

    public String name,sid,department,mobilenumber,photourl;

    public Student(String name, String sid, String department, String mobilenumber, String photourl) {
        this.name = name;
        this.sid = sid;
        this.department = department;
        this.mobilenumber = mobilenumber;
        this.photourl = photourl;
    }

    public String getName() {
        return name;
    }

    public String getSid() {
        return sid;
    }

    public String getDepartment() {
        return department;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }
}
