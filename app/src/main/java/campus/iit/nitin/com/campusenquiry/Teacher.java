package campus.iit.nitin.com.campusenquiry;

/**
 * Created by nitinsinghal on 21/05/19.
 */

public class Teacher {


    public String name,empid,location,department,officenumber,mobilenumber,profilephotourl,email;


    public Teacher(){

    }

    public Teacher(String name, String empid, String location, String department, String officenumber, String mobilenumber, String profilephotourl,String email) {
        this.name = name;
        this.empid = empid;
        this.location = location;
        this.department = department;
        this.officenumber = officenumber;
        this.mobilenumber = mobilenumber;
        this.profilephotourl = profilephotourl;
        this.email=email;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmpid() {
        return empid;
    }

    public String getLocation() {
        return location;
    }

    public String getDepartment() {
        return department;
    }

    public String getOfficenumber() {
        return officenumber;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public String getProfilephotourl() {
        return profilephotourl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setOfficenumber(String officenumber) {
        this.officenumber = officenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public void setProfilephotourl(String profilephotourl) {
        this.profilephotourl = profilephotourl;
    }
}
