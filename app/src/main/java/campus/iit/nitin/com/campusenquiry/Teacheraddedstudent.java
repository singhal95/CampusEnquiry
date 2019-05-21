package campus.iit.nitin.com.campusenquiry;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class Teacheraddedstudent extends Fragment {






    myaddedstudenttouch myaddedstudenttouch;
    ListView listView;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    DatabaseReference myRef1;
    SharedPreferences database;
    SharedPreferences.Editor editor;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> studentid;
    ArrayList<String> student_remarks;
    ArrayList<String> studentname;
    @SuppressLint("ValidFragment")
    public Teacheraddedstudent(myaddedstudenttouch myaddedstudenttouch) {
        this.myaddedstudenttouch=myaddedstudenttouch;
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
      View view= inflater.inflate(R.layout.fragment_teacheraddedstudent, container, false);
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        myRef1 = firebasedatabase.getReference("Students");
        database = getContext().getSharedPreferences("TEACHER", MODE_PRIVATE);
        editor = database.edit();
        studentid = new ArrayList<>();
        student_remarks=new ArrayList<>();
        studentname=new ArrayList<>();
        listView = view.findViewById(R.id.list);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,studentname);
        listView.setAdapter(arrayAdapter);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting List........");
        progressDialog.show();
        myRef.child(database.getString("userid", "TEST")).child("Student").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                    studentid.add(dataSnapshot1.getKey());
                    student_remarks.add(dataSnapshot1.getValue(String.class));
                    myRef1.child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            studentname.add(dataSnapshot.child("name").getValue(String.class));
                            Log.i("nitin123",studentname.get(studentname.size()-1));
                            arrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    Log.i("nitin123",String.valueOf(studentid.size()));

                    progressDialog.dismiss();
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editor.putString("Studentname", studentname.get(position));
                editor.putString("Studentremarks", student_remarks.get(position));
                editor.putString("studentid", studentid.get(position));
                editor.commit();
                myaddedstudenttouch.onmyaddedstudenttouch();
            }
        });


        return view;
    }

    public interface myaddedstudenttouch{
        public void onmyaddedstudenttouch();
    }



}
