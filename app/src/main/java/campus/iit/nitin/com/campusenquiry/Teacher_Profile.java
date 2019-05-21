package campus.iit.nitin.com.campusenquiry;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Teacher_Profile extends Fragment {



    TextView department,name;
    Button send;
    EditText remarks;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    private SharedPreferences database;
    private SharedPreferences.Editor editor;
    String value;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_teacher__profile, container, false);
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        database=getContext().getSharedPreferences("STUDENT",MODE_PRIVATE);
        editor=database.edit();
        department=view.findViewById(R.id.departmentvalue);
        name=view.findViewById(R.id.namevalue);
        send=view.findViewById(R.id.send);
        remarks=view.findViewById(R.id.remarks);
        name.setText(database.getString("teachername","TEST"));
        department.setText(database.getString("teacherdepartment","TEST"));
        myRef.child(database.getString("teacherid","TEST")).child("Request").child(database.getString("userid","TEST")).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 value=dataSnapshot.getValue(String.class);
                 if(value!=null) {
                     if (value.equals(database.getString("email", "TEST"))) {
                         send.setText("Request Send");
                         send.setEnabled(false);
                     }
                 }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remaksvalue=remarks.getText().toString();
                String email=database.getString("email","TEST");
                String uid=database.getString("userid","TEST");
                Request request=new Request(email,remaksvalue);
                myRef.child(database.getString("teacherid","TEST")).child("Request").child(uid).setValue(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),"Successfully Request send",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        return view;
    }




}
