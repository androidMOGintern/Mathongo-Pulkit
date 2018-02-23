package com.learnacad.learnacad.Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.learnacad.learnacad.Activities.CheckoutActivity;
import com.learnacad.learnacad.Models.Student;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyReferals_Fragment extends Fragment {

    Unbinder unbinder;

    @BindView(R.id.referals_noofcoinstextview)
    TextView coinTextView;

    @BindView(R.id.referal_sharecode)
    TextView sharecodeTextView;

    @BindView(R.id.referals_sharebutton)
    ImageButton whatsappShareBtn;

    @BindView(R.id.referal_addmorecoin)
    TextView addmorecoins;

    DatabaseReference myRootref;
    Integer Coins;
    Student student;


    String referal_code;



    public MyReferals_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.myreferals_home_layout, container, false);
        unbinder = ButterKnife.bind(this, view);

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();



        getActivity().setTitle("My Referals");
        List<Student> students = SugarRecord.listAll(Student.class);
        if (students != null && students.size() > 0)
            student = students.get(0);
        myRootref = FirebaseDatabase.getInstance().getReference("users/"+student.getMobileNum());

        myRootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Coins = dataSnapshot.child("coins").getValue(Integer.class);
                coinTextView.setText(dataSnapshot.child("coins").getValue(Integer.class).toString());
                referal_code = dataSnapshot.child("usedReferalCode").getValue(String.class);
                sharecodeTextView.setText(dataSnapshot.child("usedReferalCode").getValue(String.class));
                pDialog.dismissWithAnimation();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        whatsappShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share" + referal_code);
                try {
                    getActivity().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addmorecoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),CheckoutActivity.class);
                i.putExtra("studentid",student.getMobileNum());
                startActivity(i);
            }
        });


        return view;

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



}
