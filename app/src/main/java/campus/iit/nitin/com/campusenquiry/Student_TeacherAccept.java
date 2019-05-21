package campus.iit.nitin.com.campusenquiry;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class Student_TeacherAccept extends Fragment {



    teacheraccepttouch teacheraccepttouch;
    ListView listView;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    DatabaseReference myRef1;
    SharedPreferences database;
    SharedPreferences.Editor editor;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> teacherid;
    ArrayList<String> teacher_name;
    @SuppressLint("ValidFragment")
    public Student_TeacherAccept(teacheraccepttouch teacheraccepttouch) {
        // Required empty public constructor
        this.teacheraccepttouch=teacheraccepttouch;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_student__teacher_accept, container, false);
        firebasedatabase = FirebaseDatabase.getInstance();
        myRef = firebasedatabase.getReference("Teachers");
        myRef1 = firebasedatabase.getReference("Students");
        database = getContext().getSharedPreferences("STUDENT", MODE_PRIVATE);
        editor = database.edit();
        teacherid = new ArrayList<>();
        teacher_name=new ArrayList<>();
        listView = view.findViewById(R.id.list);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,teacher_name);
        listView.setAdapter(arrayAdapter);
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Getting List........");
        progressDialog.show();
        myRef1.child(database.getString("userid", "TEST")).child("Teacher").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                    teacherid.add(dataSnapshot1.getKey());
                    myRef.child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            teacher_name.add(dataSnapshot.child("name").getValue(String.class));
                            arrayAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


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
                editor.putString("teachername",teacher_name.get(position));
                editor.putString("teacherid", teacherid.get(position));
                editor.commit();
                teacheraccepttouch.onteacheraccepttouch();
            }
        });

        return view;
    }


    public interface teacheraccepttouch{
        public void onteacheraccepttouch();
    }


}
