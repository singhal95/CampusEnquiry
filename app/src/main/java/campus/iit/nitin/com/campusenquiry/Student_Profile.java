package campus.iit.nitin.com.campusenquiry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class Student_Profile extends Fragment {


    ImageView imageView;
    TextView name, sid, department, mobilenumber, remark;
    Button Accept, decline;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    DatabaseReference myRef1;
    private FusedLocationProviderClient fusedLocationClient;
    private Geocoder geocoder;
    List<Address> addresses;
    SharedPreferences database;
    SharedPreferences.Editor editor;

    public Student_Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_student__profile, container, false);
        imageView = view.findViewById(R.id.displayimage);
        name = view.findViewById(R.id.namevalue);
        sid = view.findViewById(R.id.sid);
        firebasedatabase = FirebaseDatabase.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        myRef = firebasedatabase.getReference("Teachers");
        myRef1 = firebasedatabase.getReference("Students");
        database = getContext().getSharedPreferences("TEACHER", MODE_PRIVATE);
        editor = database.edit();
        department = view.findViewById(R.id.departmentvalue);
        mobilenumber = view.findViewById(R.id.mobilenumbervalue);
        remark = view.findViewById(R.id.remarks);
        Accept = view.findViewById(R.id.accept);
        decline = view.findViewById(R.id.decline);
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlocation();
                myRef.child(database.getString("userid", "TEST")).child("Request").child(database.getString("studentid", "TEST")).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        myRef.child(database.getString("userid", "TEST")).child("Student").child(database.getString("studentid", "TEST")).setValue(database.getString("Studentremarks", "TEST")).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                myRef1.child(database.getString("studentid", "TEST")).child("Teacher").child(database.getString("userid", "TEST")).setValue(database.getString("email", "TEST")).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Successfull added", Toast.LENGTH_SHORT).show();
                                        myRef.child(database.getString("userid", "TEST")).child("location").setValue("hrllo").addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getContext(), "Location Suceessfully updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }
                        });

                    }
                });

            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    @SuppressLint("MissingPermission")
    public String getlocation() {
        final String[] completeaddress=new String[1];

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
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
                                completeaddress[0]=address + " " + city + " " + state + " " + country + " " + postalCode;



                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
        return completeaddress[0];
    }
}
