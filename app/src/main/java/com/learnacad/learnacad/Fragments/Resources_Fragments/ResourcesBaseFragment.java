package com.learnacad.learnacad.Fragments.Resources_Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.learnacad.learnacad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.learnacad.learnacad.Fragments.LCCMaterialsFragment.PERMISSION_REQUEST_CODE;

/**
 * Created by sahil on 25/12/17.
 */

public class ResourcesBaseFragment extends Fragment {

    Unbinder unbinder;
    View view;

    @BindView(R.id.formulaSheet_CardView_ResourcesFragment)
    CardView formulaCardView;

    @BindView(R.id.prevYear_CardView_ResourcesFragment)
    CardView previousYearCardView;

    @BindView(R.id.ncert_CardView_ResourcesFragment)
    CardView ncertCardView;

    @BindView(R.id.questionBank_CardView_ResourcesFragment)
    CardView questionBankCardView;

    @BindView(R.id.books_CardView_ResourcesFragment)
    CardView booksCardView;

    Context mContext;
    ProgressDialog pd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.resources_base_fragment_layout,container,false);
        unbinder = ButterKnife.bind(this,view);

        pd = new ProgressDialog(getActivity());
        pd.setCancelable(false);

        getActivity().setTitle("Resources");

        mContext = getActivity();

        if(!checkPermission()){

            requestPermission();
        }
        return view;
    }

    private void requestPermission() {

        requestPermissions(new String[]{READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }


    private boolean checkPermission() {

        int readExternal = ContextCompat.checkSelfPermission(mContext.getApplicationContext(), READ_EXTERNAL_STORAGE);
        int writeExternal = ContextCompat.checkSelfPermission(mContext.getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return (readExternal == PackageManager.PERMISSION_GRANTED && writeExternal == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        formulaCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new FormulaFragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });


        previousYearCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new PreviousYearFragment());
                fragmentTransaction.addToBackStack(null).commit();


            }
        });

        ncertCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new NCERTFragment());
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

        questionBankCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new QuestionBankFragment());
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

        booksCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new BooksFragment());
                fragmentTransaction.addToBackStack(null).commit();

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
