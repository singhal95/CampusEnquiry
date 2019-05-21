package campus.iit.nitin.com.campusenquiry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.Context.MODE_PRIVATE;


public class Student_Teacher_Declineprofile extends Fragment {


    TextView department,name,remarks;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    private SharedPreferences database;
    private SharedPreferences.Editor editor;

    public Student_Teacher_Declineprofile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_student__teacher__declineprofile, container, false);
        department=view.findViewById(R.id.departmentvalue);
        name=view.findViewById(R.id.namevalue);
        remarks=view.findViewById(R.id.remarks);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting Data........");
        progressDialog.show();
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        database = getContext().getSharedPreferences("STUDENT", MODE_PRIVATE);
        editor = database.edit();
        remarks.setText(database.getString("teacher_remark","TEST"));
        name.setText(database.getString("teachername","TEST"));
        myRef.child(database.getString("teacherid","TEST")).child("department").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value=dataSnapshot.getValue(String.class);
                if(value!=null) {
                  department.setText(value);
                  progressDialog.dismiss();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }


}
