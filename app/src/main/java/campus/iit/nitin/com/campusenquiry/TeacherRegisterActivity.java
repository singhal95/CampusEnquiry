package campus.iit.nitin.com.campusenquiry;

import android.*;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.security.Permission;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherRegisterActivity extends AppCompatActivity {


    private Button save;
    private EditText name, officenumber, mobilenumber, employeid;
    private TextView locationvalue, department;
    private CircleImageView profilephoto;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    List<Address> addresses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_register);
        save = findViewById(R.id.save);
        name = findViewById(R.id.namevalue);
        officenumber = findViewById(R.id.officenumbervalue);
        mobilenumber = findViewById(R.id.mobilenumbervalue);
        employeid = findViewById(R.id.empid);
        locationvalue = findViewById(R.id.locationvalue);
        department = findViewById(R.id.departmentvalue);
        profilephoto = findViewById(R.id.displayimage);
        geocoder = new Geocoder(this, Locale.getDefault());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(TeacherRegisterActivity.this);
        locationvalue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(TeacherRegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TeacherRegisterActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }

                }
                else {
                    getlocation();
                }
            }
        });
        department.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(TeacherRegisterActivity.this);
                builderSingle.setTitle("Select Department:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TeacherRegisterActivity.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("CSE");
                arrayAdapter.add("IT");
                arrayAdapter.add("ECE");
                arrayAdapter.add("EEE");
                arrayAdapter.add("ME");
                arrayAdapter.add("Civil");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(TeacherRegisterActivity.this);
                        builderInner.setMessage(strName);
                        department.setText(strName);
                        builderInner.setTitle("Your Selected Item is");
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();





            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                getlocation();
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

        }
    }

    @SuppressLint("MissingPermission")
    public void getlocation(){
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String state = addresses.get(0).getAdminArea();
                                String country = addresses.get(0).getCountryName();
                                String postalCode = addresses.get(0).getPostalCode();
                                String completeaddress=address+" "+city+" "+state+" "+country+" "+postalCode;
                                locationvalue.setText(completeaddress);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    public void onsave(){

    }

    public boolean validate(){
        boolean valid=true;
        if(name.getText().toString().isEmpty()){
            valid=false;
            name.setError("Please enter the name");
        }
        else {
            name.setError(null);
            valid=true;
        }
        if (employeid.getText().toString().isEmpty()){
            valid=false;
            name.setError("Please enter the employee id");

        }
        else {
            name.setError(null);
            valid=true;
        }
        if (locationvalue.getText().toString().isEmpty()){
            valid=false;
            locationvalue.setError("Please enter location");

        }
        else {
          locationvalue.setError(null);
            valid=true;
        }
        if (department.getText().toString().isEmpty()){
            valid=false;
            department.setError("Please enter the department");

        }
        else {
            department.setError(null);
            valid=true;
        }
        if (officenumber.getText().toString().isEmpty()){
            valid=false;
            officenumber.setError("Please enter the officenumber");

        }
        else {
            officenumber.setError(null);
            valid=true;
        }
        if (mobilenumber.getText().toString().isEmpty()){
            valid=false;
           mobilenumber.setError("Please enter the mobilenumber");

        }
        else {
            mobilenumber.setError(null);
            valid=true;
        }
        return valid;
    }
}
