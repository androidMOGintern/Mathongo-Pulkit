package com.learnacad.learnacad.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.learnacad.learnacad.Adapters.LCCLecturesListAdapter;
import com.learnacad.learnacad.Models.Lecture;
import com.learnacad.learnacad.Models.MyCoursesEnrolled;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.Tutor;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.orm.SugarRecord.listAll;

/**
 * Created by Sahil Malhotra on 22-06-2017.
 */

public class LCCLecturesFragment extends Fragment {

    private static View view;
    private RecyclerView recyclerView;
    private LCCLecturesListAdapter lccLecturesListAdapter;
    ArrayList<Lecture> lectures;
    ProgressBar progressBar;
    boolean isEnrolled;
    MyCoursesEnrolled coursesEnrolled;
    int course_id;
    Tutor t;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lcclectures_fragment_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.lcclecturesFragmentRecyclerView);
        lectures = new ArrayList<>();
        course_id = getActivity().getIntent().getIntExtra("MINICOURSE_ID", 0);
        t = new Tutor();
        isEnrolled = checkEnrolled();
        lccLecturesListAdapter = new LCCLecturesListAdapter(getActivity(), lectures, t,isEnrolled, (CoordinatorLayout) getActivity().findViewById(R.id.cordinatorLayout_library_course_content));
        recyclerView.setAdapter(lccLecturesListAdapter);
        progressBar = view.findViewById(R.id.pb);
        progressBar.setIndeterminate(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchData();

        return view;
    }


    private boolean checkEnrolled() {

        List<MyCoursesEnrolled> enrolledList = listAll(MyCoursesEnrolled.class);

        String toParseCoursesString = "[]";

        if(enrolledList != null && enrolledList.size() > 0){

            toParseCoursesString = enrolledList.get(enrolledList.size() - 1).getMycourses();

        }
        try {
            JSONArray array= new JSONArray(toParseCoursesString);
            for(int i = 0; i < array.length(); ++i){

                int id = array.getInt(i);
                if(id == course_id){

                    return true;
                }
            }

        } catch (JSONException e) {
            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                    .setContentText("There seems a problem with us.\nPlease try again later.(101LC_LF_CE)")
                    .setTitleText("Oops..!!")
                    .show();
        }

        return false;

    }


    private void fetchData() {

        progressBar.setVisibility(View.VISIBLE);

        List<SessionManager> session = listAll(SessionManager.class);
        AndroidNetworking.get(Api_Urls.BASE_URL + "api/minicourses/" + course_id)
                .addHeaders("Authorization", "bearer" + session.get(0).getToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            JSONObject tutor = response.getJSONObject("tutor");
                            String teacherName = tutor.getString("name");
                            String teachersdescription = tutor.getString("description");
                            int teachersId = tutor.getInt("id");
                            t.setName(teacherName);
                            t.setDescription(teachersdescription);
                            t.setTutor_id(teachersId);

                            JSONArray lessons = response.getJSONArray("lessons");

                            for (int i = 0; i < lessons.length(); ++i) {

                                JSONObject lesson = lessons.getJSONObject(i);
                                String lesson_name = lesson.getString("name");
                                String url = lesson.getString("videoUrl");
                                String lesson_duration = lesson.getString("duration");
                                int lesson_id = lesson.getInt("id");
                                String lesson_description = lesson.getString("description");
                                int upvotes = lesson.getInt("upvotes");

                                Lecture lecture = new Lecture(lesson_id, lesson_name, url, lesson_duration, lesson_description,upvotes,false,false);
                                lectures.add(lecture);
                            }
                            if(isEnrolled){

                                lccLecturesListAdapter.isEnrolledChanged();
                            }
                            lccLecturesListAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LC_LF_MI)")
                                    .setTitleText("Oops..!!")
                                    .show();
                            progressBar.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LC_LF_MI)")
                                .setTitleText("Oops..!!")
                                .show();
                        progressBar.setVisibility(View.GONE);


                    }
                });
    }
}