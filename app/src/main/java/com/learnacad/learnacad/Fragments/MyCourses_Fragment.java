package com.learnacad.learnacad.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.learnacad.learnacad.Adapters.LibraryCourseListAdapter;
import com.learnacad.learnacad.Models.Minicourse;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.Tutor;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.orm.SugarRecord.listAll;

/**
 * Created by Sahil Malhotra on 03-06-2017.
 */

public class MyCourses_Fragment extends Fragment {


    private static View view;
    RecyclerView recyclerView;
    ArrayList<Minicourse> minicoursesList;
    ArrayList<Tutor> tutors;
    LibraryCourseListAdapter listAdapter;
    ProgressBar progressBar;
    RelativeLayout emptyStateLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view != null){

            Log.d("lolo","Inside MyCourses_Fragment IF");
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){

                parent.removeView(view);
            }
        }


        try {
            view = inflater.inflate(R.layout.mycourses_home_layout,container,false);


        }catch (InflateException e){

            Log.d("lolo","Inside MyCourses_Fragment CATCH");
        }


        if(!isConnected()){

            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Connection Error!\nPlease try again later.")
                    .setTitleText("Oops..!!")
                    .show();

        }

        recyclerView = (RecyclerView) view.findViewById(R.id.mycoursesRecyclerView);
        minicoursesList = new ArrayList<>();
        tutors = new ArrayList<>();
        listAdapter = new LibraryCourseListAdapter(getActivity(),minicoursesList,null,tutors);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressBar = view.findViewById(R.id.pb);
        progressBar.setIndeterminate(true);;
       emptyStateLayout = (RelativeLayout) view.findViewById(R.id.mycoursesemptyLayout);

        getActivity().setTitle("My Courses");
        fetchData();
        return view;
    }

    private void fetchData() {


        progressBar.setVisibility(View.VISIBLE);

        final HashMap<Integer,Boolean> map = new HashMap<>();

        List<SessionManager> session = listAll(SessionManager.class);
        AndroidNetworking.get(Api_Urls.BASE_URL + "api/students/mycourses")
                .addHeaders("Authorization","bearer " + session.get(0).getToken())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response.length() == 0){

                           emptyStateLayout.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }else{

                            emptyStateLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }




                            for(int j = 0; j < response.length(); ++j) {

                                try {
                                    JSONObject outerObject = response.getJSONObject(j);
                                    int id = outerObject.getInt("minicourseId");

                                    if (!map.containsKey(id)) {

                                        map.put(id, true);


                                        JSONObject object = outerObject.getJSONObject("minicourse");

                                        String isActive = object.getString("isActive");

                                        if(!isActive.contentEquals("true")){

                                            continue;
                                        }


                                        int minicourse_id = object.getInt("id");
                                        String minicourse_name = object.getString("name");
                                        String minicourse_description = object.getString("description");
                                        float minicourse_rating = (float) object.getDouble("rating");

                                        Minicourse minicourse = new Minicourse();
                                        minicourse.setCourse_id(minicourse_id);
                                        minicourse.setName(minicourse_name);
                                        minicourse.setDescription(minicourse_description);
                                        minicourse.setRating(minicourse_rating);

                                        StringBuilder builder = new StringBuilder();


                                        JSONArray categories = object.getJSONArray("minicoursecategories");
                                        for (int k = 0; k < categories.length(); ++k) {

                                            JSONObject catObject = categories.getJSONObject(k);
                                            JSONObject lastObjtoCat = catObject.getJSONObject("category");

                                            builder.append(lastObjtoCat.getString("categoryName"));

                                        }

                                        minicourse.setEnrolled(false);
                                        minicourse.setRelevance(builder.toString());


                                        JSONObject tutorObj = object.getJSONObject("tutor");
                                        Tutor tutor = new Tutor(tutorObj.getInt("id"), tutorObj.getString("name"), tutorObj.getString("description"));
                                        String teachersUrl = tutorObj.getString("img");
                                        tutor.setImgUrl(teachersUrl);
                                        tutors.add(tutor);

                                        minicourse.setTutorImageUrl(tutor.getImgUrl());
                                        minicourse.setTutorName(tutor.getName());

                                        minicoursesList.add(minicourse);


                                    }
                                }catch (JSONException e){
                                            continue;
                                }
                            }

                            listAdapter.notifyDataSetChanged();


                        progressBar.setVisibility(View.GONE);


                    }

                    @Override
                    public void onError(ANError anError) {

                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.")
                                .setTitleText("Oops..!!")
                                .show();
                        progressBar.setVisibility(View.GONE);

                    }
                });


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }
}
