package com.learnacad.learnacad.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.learnacad.learnacad.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sahil on 27/12/17.
 */

public class SignUp_Step2b_Fragment extends Fragment {

    Bundle bundle;

    View view;
    Unbinder unbinder;

    @BindView(R.id.jeeMainsCardView_signUp_step2b)
    CardView jeeMainsCardView;

    @BindView(R.id.jeeAdvanceCardView_signUp_step2b)
    CardView jeeAdvanceCardView;

    @BindView(R.id.boardExams_signUp_step2b)
    CardView boardExamsCardView;

    @BindView(R.id.otherExams_signUp_step2b)
    CardView otherExamsCardView;

    @BindView(R.id.bitsatCardView_signUp_step2b)
    CardView bitsatCardView;

    @BindView(R.id.jeeMainsTextView)
    TextView jeeMainsTextView;

    @BindView(R.id.jeeAdvanceTextView)
    TextView jeeAdvanceTextView;

    @BindView(R.id.bitsatTextView)
    TextView bitsatTextView;

    @BindView(R.id.boardsTextView)
    TextView boardExamsTextView;

    @BindView(R.id.otherTextView)
    TextView otherExamsTextView;

    @BindView(R.id.signUp_step2b_submitButton)
    Button continueButton;

    ArrayList<String>choices;

    int color,grayColor;

    StringBuilder stringBuilder;
    boolean isJeeMainSelected,isJeeAdvanceSelected,isBitsatSelected,isBoardsSelected,isOtherExamSelected;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sign_up_step2b_layout,container,false);

        bundle = this.getArguments();

        unbinder = ButterKnife.bind(this,view);


        color = Color.parseColor("#1589ee");
        grayColor = Color.parseColor("#6885a5");
        choices = new ArrayList<>();
        isJeeAdvanceSelected = isJeeMainSelected = isBitsatSelected = isBoardsSelected = isOtherExamSelected = false;

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {

        jeeMainsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isJeeMainSelected) {

                    isJeeMainSelected = true;
                    jeeMainsCardView.setCardBackgroundColor(color);
                    jeeMainsTextView.setTextColor(color);
                    choices.add("Jee Main");
                }else{

                    isJeeMainSelected = false;
                    jeeMainsCardView.setCardBackgroundColor(Color.WHITE);
                    jeeMainsTextView.setTextColor(grayColor);
                    choices.remove("Jee Main");
                }
            }
        });

        jeeAdvanceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isJeeAdvanceSelected) {

                    isJeeAdvanceSelected = true;
                    jeeAdvanceCardView.setCardBackgroundColor(color);
                    jeeAdvanceTextView.setTextColor(color);
                    choices.add("Jee Advance");
                }else{

                    isJeeAdvanceSelected = false;
                    jeeAdvanceCardView.setCardBackgroundColor(Color.WHITE);
                    jeeAdvanceTextView.setTextColor(grayColor);
                    choices.remove("Jee Advance");
                }
            }
        });

        boardExamsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isBoardsSelected) {

                    isBoardsSelected = true;
                    boardExamsCardView.setCardBackgroundColor(color);
                    boardExamsTextView.setTextColor(color);
                    choices.add("Boards");
                }else{

                    isBoardsSelected = false;
                    boardExamsCardView.setCardBackgroundColor(Color.WHITE);
                    boardExamsTextView.setTextColor(grayColor);
                    choices.remove("Boards");

                }
            }
        });

        bitsatCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isBitsatSelected) {

                    isBitsatSelected = true;
                    bitsatCardView.setCardBackgroundColor(color);
                    bitsatTextView.setTextColor(color);
                    choices.add("BITSAT");
                }else{

                    isBitsatSelected = false;
                    bitsatCardView.setCardBackgroundColor(Color.WHITE);
                    bitsatTextView.setTextColor(grayColor);
                    choices.remove("BITSAT");

                }
            }
        });

        otherExamsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isOtherExamSelected) {

                    isOtherExamSelected = true;
                    otherExamsCardView.setCardBackgroundColor(color);
                    otherExamsTextView.setTextColor(color);
                    choices.add("Other Exams");
                }else{

                    isOtherExamSelected = false;
                    otherExamsCardView.setCardBackgroundColor(Color.WHITE);
                    otherExamsTextView.setTextColor(grayColor);
                    choices.remove("Other Exams");
                }
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(choices.size() > 0){

                    stringBuilder = new StringBuilder();
                    stringBuilder.append("[");

                    for(int i = 0; i < choices.size(); ++i){

                        stringBuilder.append(choices.get(i));
                        stringBuilder.append(",");
                    }

                    stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                    stringBuilder.append("]");

                    bundle.putString("prefered_exam",stringBuilder.toString());

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                    SignUp_Step3_Fragment fragment = new SignUp_Step3_Fragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.content_login_frame,fragment);
                    fragmentTransaction.addToBackStack(null).commit();
                }else{

                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Please enter a valid OTP")
                            .setTitleText("Nothing Selected")
                            .show();

//
//                    Snackbar snackbar1 = Snackbar.make(view,"Please select atleast one target exam to continue.",Snackbar.LENGTH_LONG);
//                    View view1 = snackbar1.getView();
//                    TextView textView = (TextView) view1.findViewById(android.support.design.R.id.snackbar_text);
//                    view1.setPadding(0,0,0,0);
//                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.redcircle,0,0,0);
//                    textView.setCompoundDrawablePadding(8);
//                    snackbar1.show();
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
