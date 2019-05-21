package campus.iit.nitin.com.campusenquiry;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


@SuppressLint("ValidFragment")
public class Student_teacher extends Fragment {


    private Button choosedepartment;
    String department;
    FirebaseDatabase firebasedatabase;
    DatabaseReference myRef;
    private SharedPreferences database;
    private SharedPreferences.Editor editor;
    ArrayList<String> teacherid;
    ArrayList<Teacher> teacherdetails;
    ArrayList<String> teachername;
    ListView listView;
    itemtouch itemtouch;
    ArrayAdapter<String> arrayAdapter1;

    @SuppressLint("ValidFragment")
    public Student_teacher(itemtouch itemtouch){
        this.itemtouch=itemtouch;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_student_teacher, container, false);
        choosedepartment=view.findViewById(R.id.choosedepartment);
        firebasedatabase = FirebaseDatabase.getInstance();
        listView=view.findViewById(R.id.list);
        teachername=new ArrayList<>();
        myRef = firebasedatabase.getReference("Teachers");
        database=getContext().getSharedPreferences("STUDENT",MODE_PRIVATE);
    arrayAdapter1=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,teachername);
        listView.setAdapter(arrayAdapter1);
        editor=database.edit();
        teacherid=new ArrayList<>();
        teacherdetails=new ArrayList<>();
        choosedepartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle("Select Department:-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_singlechoice);
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
                        final String strName = arrayAdapter.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                        builderInner.setMessage(strName);
                        department=strName;
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
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Wait.....");
                progressDialog.show();

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> dataSnapshots=dataSnapshot.getChildren();
                        for(DataSnapshot dataSnapshot1: dataSnapshots){
                            Teacher value = dataSnapshot1.getValue(Teacher.class);
                            if(value.getDepartment().equals(department)) {
                                teacherid.add(dataSnapshot1.getKey());
                                teacherdetails.add(value);
                                teachername.add(value.getName());

                            }
                            progressDialog.dismiss();
                            arrayAdapter1.notifyDataSetChanged();


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                progressDialog.dismiss();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        editor.putString("teachername",teacherdetails.get(position).getName());
                        editor.putString("teacherdepartment",teacherdetails.get(position).getDepartment());
                        editor.putString("teacherid",teacherid.get(position));
                        editor.commit();
                        itemtouch.onitemtouch();



                    }
                });


            }
        });




        return view;
    }


    public interface itemtouch{
        public void onitemtouch();
    }


}
