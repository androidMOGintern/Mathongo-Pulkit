package com.learnacad.learnacad.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.learnacad.learnacad.Models.CONSTANTS;
import com.learnacad.learnacad.Models.Filter;
import com.learnacad.learnacad.Models.FiltersViewModel;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

/**
 * Created by Sahil Malhotra on 18-06-2017.
 */

public class Filters extends Fragment {

    View view;
    Filter filter;
    Bundle checkedSaving;

    FiltersViewModel mViewModel;

      CheckBox subjectPhysicsCB;
      CheckBox subjectMathsCB;
      CheckBox subjectChemistryCB;

      CheckBox categoryMainsCB;
      CheckBox categoryAdvancedCB;
      CheckBox categorycbseCB;

      CheckBox classXIICB;
      CheckBox classXICB;
      CheckBox classXCB;

      CheckBox diffBeginnerCB;
      CheckBox diffIntermediateCB;
      CheckBox diffAdvancedCB;

      CheckBox mediumEnglishCB;
      CheckBox mediumHindiCB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.filter,container,false);

        if(savedInstanceState != null){

            Bundle b = savedInstanceState.getBundle("filtersSaved");
            Log.d("toing", String.valueOf(b.size()));
        }

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBundle("filtersSaved",checkedSaving);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        filter = new Filter();


//        if(savedInstanceState != null){
//
//            Bundle b = savedInstanceState.getBundle("filtersSaved");
//
//            Log.d("toing", String.valueOf(b.size()));
//        }


        subjectPhysicsCB = view.findViewById(R.id.filterSubjectPhysicsCheckBox);
        subjectMathsCB = view.findViewById(R.id.filterSubjectMathsCheckBox);
        subjectChemistryCB = view.findViewById(R.id.filterSubjectChemistryCheckBox);

        categoryMainsCB = view.findViewById(R.id.filterCategoryMainsCheckBox);
        categoryAdvancedCB = view.findViewById(R.id.filterCategoryAdvancedCheckBox);
        categorycbseCB = view.findViewById(R.id.filterCategoryCBSECheckBox);

        classXIICB = view.findViewById(R.id.filterClass12CheckBox);
        classXICB = view.findViewById(R.id.filterClass11CheckBox);
        classXCB = view.findViewById(R.id.filterClass10CheckBox);

        diffBeginnerCB = view.findViewById(R.id.filterDifficultyBeginner);
        diffIntermediateCB = view.findViewById(R.id.filterDifficultyIntermediate);
        diffAdvancedCB = view.findViewById(R.id.filterDifficultyAdvanced);

        mediumEnglishCB = view.findViewById(R.id.filterMediumEnglish);
        mediumHindiCB = view.findViewById(R.id.filterMediumHindi);

        mViewModel = ViewModelProviders.of(getActivity()).get(FiltersViewModel.class);
        setCheckedOptions();





        Button resetButton = view.findViewById(R.id.filterResetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subjectPhysicsCB.setChecked(false);
                subjectMathsCB.setChecked(false);
                subjectChemistryCB.setChecked(false);
                categoryMainsCB.setChecked(false);
                categorycbseCB.setChecked(false);
                categoryAdvancedCB.setChecked(false);
                classXCB.setChecked(false);
                classXICB.setChecked(false);
                classXIICB.setChecked(false);
                diffAdvancedCB.setChecked(false);
                diffBeginnerCB.setChecked(false);
                diffIntermediateCB.setChecked(false);
                mediumEnglishCB.setChecked(false);
                mediumHindiCB.setChecked(false);
            }
        });


        ImageButton cancelFilter = view.findViewById(R.id.cancelFilterButton);
        cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.content_frame,new LibraryCourseListFragment());
                ft.commit();

            }
        });

        Button applyButton = view.findViewById(R.id.filterApplyButton);


        final Bundle checkedItemsbundle = new Bundle();
        final ArrayList<String> checked = new ArrayList<>();


        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(subjectPhysicsCB.isChecked()){


                    filter.getSubjectArray().add(String.valueOf(CONSTANTS.SUBJECT_PHYSICS));
                    checked.add("Physics");
                    mViewModel.setSubjectPhysicsCB(true);
                }else{


                    mViewModel.setSubjectPhysicsCB(false);
                }

                if(subjectChemistryCB.isChecked()){

                    filter.getSubjectArray().add(String.valueOf(CONSTANTS.SUBJECT_CHEMISTRY));
                    checked.add("Chemistry");
                    mViewModel.subjectChemistryCB = true;


                }else{

                    mViewModel.setSubjectChemistryCB(false);
                }

                if(subjectMathsCB.isChecked()){

                    filter.getSubjectArray().add(String.valueOf(CONSTANTS.SUBJECT_MATHS));
                    checked.add("Maths");
                    mViewModel.subjectMathsCB = true;


                }else{

                    mViewModel.setSubjectMathsCB(false);
                }



                if(categoryMainsCB.isChecked()){

                    filter.getCategoryArray().add(String.valueOf(CONSTANTS.CATEGORY_MAINS));
                    checked.add("JEE Mains");
                    mViewModel.categoryMainsCB = true;



                }else{

                    mViewModel.setCategoryMainsCB(false);
                }

                if(categorycbseCB.isChecked()){

                    filter.getCategoryArray().add(String.valueOf(CONSTANTS.CATEGORY_CBSE));
                    checked.add("CBSE");
                    mViewModel.categorycbseCB = true;


                }else{

                    mViewModel.setCategorycbseCB(false);
                }

                if(categoryAdvancedCB.isChecked()){

                    filter.getCategoryArray().add(String.valueOf(CONSTANTS.CATEGORY_ADVANCED));
                    mViewModel.categoryAdvancedCB = true;
                    checked.add("JEE Advanced");


                }else{


                    mViewModel.setCategoryAdvancedCB(false);
                }



                if(classXIICB.isChecked()){

                    filter.getClassArray().add(String.valueOf(CONSTANTS.CLASS_12));
                    mViewModel.classXIICB = true;


                    checked.add("XII");
                }else{

                    mViewModel.setClassXIICB(false);
                }

                if(classXICB.isChecked()){

                    filter.getClassArray().add(String.valueOf(CONSTANTS.CLASS_11));
                    mViewModel.classXICB = true;


                    checked.add("XI");
                }else{

                    mViewModel.setClassXICB(false);
                }

                if(classXCB.isChecked()){

                    filter.getClassArray().add(String.valueOf(CONSTANTS.CLASS_10));
                    mViewModel.classXCB = true;
                    checked.add("X");


                }else{

                    mViewModel.setClassXCB(false);
                }



                if(diffBeginnerCB.isChecked()){

                    filter.getDifficultyArray().add("Beginner");

                    mViewModel.diffBeginnerCB = true;


                    checked.add("Beginner");
                }else{

                    mViewModel.setDiffBeginnerCB(false);
                }

                if(diffIntermediateCB.isChecked()){

                    filter.getDifficultyArray().add("Intermediate");
                    mViewModel.diffIntermediateCB = true;


                    checked.add("Intermediate");
                }else{

                    mViewModel.setDiffIntermediateCB(false);
                }



                if(diffAdvancedCB.isChecked()){

                    filter.getDifficultyArray().add("Advanced");
                    mViewModel.diffAdvancedCB = true;
                    checked.add("Advanced");


                }else{

                    mViewModel.setDiffAdvancedCB(false);
                }



                if(mediumEnglishCB.isChecked()){

                    filter.getMediumArray().add("English");
                    mViewModel.mediumEnglishCB = true;
                    checked.add("English");


                }else{


                    mViewModel.setMediumEnglishCB(false);
                }

                if(mediumHindiCB.isChecked()){

                    filter.getMediumArray().add("Hindi");
                    mViewModel.mediumHindiCB = true;


                    checked.add("Hindi");
                }else{


                    mViewModel.setMediumHindiCB(false);
                }



                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                checkedItemsbundle.putStringArrayList("checkedItems",checked);
                checkedItemsbundle.putSerializable("filterObject",filter);
                LibraryCourseListFragment libraryCourseListFragment = new LibraryCourseListFragment();
                libraryCourseListFragment.setArguments(checkedItemsbundle);
                ft.replace(R.id.content_frame, libraryCourseListFragment,"TAG");
                ft.commit();


            }
        });


     }

     void setCheckedOptions(){

         if(mViewModel.subjectPhysicsCB){

             subjectPhysicsCB.setChecked(true);
         }

         if(mViewModel.subjectChemistryCB){

             subjectChemistryCB.setChecked(true);
         }

         if(mViewModel.subjectMathsCB){

             subjectMathsCB.setChecked(true);
         }


         if(mViewModel.categoryAdvancedCB){

             categoryAdvancedCB.setChecked(true);
         }

         if(mViewModel.categorycbseCB){

             categorycbseCB.setChecked(true);
         }

         if(mViewModel.categoryMainsCB){

             categoryMainsCB.setChecked(true);
         }

         if(mViewModel.mediumEnglishCB){

             mediumEnglishCB.setChecked(true);
         }

         if(mViewModel.mediumHindiCB){

             mediumHindiCB.setChecked(true);
         }


         if(mViewModel.classXIICB){

             classXIICB.setChecked(true);
         }

         if(mViewModel.classXCB){


             classXCB.setChecked(true);
         }

         if(mViewModel.classXICB){

             classXICB.setChecked(true);
         }


         if(mViewModel.diffAdvancedCB){

             diffAdvancedCB.setChecked(true);
         }

         if(mViewModel.diffBeginnerCB){

             diffBeginnerCB.setChecked(true);
         }

         if(mViewModel.diffIntermediateCB){

             diffIntermediateCB.setChecked(true);
         }
     }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
