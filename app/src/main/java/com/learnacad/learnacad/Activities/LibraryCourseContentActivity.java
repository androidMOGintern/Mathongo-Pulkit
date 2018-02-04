package com.learnacad.learnacad.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Adapters.LibraryCourseContentViewPagerAdapter;
import com.learnacad.learnacad.Models.Lecture;
import com.learnacad.learnacad.Models.LectureListMapping;
import com.learnacad.learnacad.Models.Minicourse;
import com.learnacad.learnacad.Models.MyCoursesEnrolled;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.Tutor;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.learnacad.learnacad.Fragments.LCCMaterialsFragment.PERMISSION_REQUEST_CODE;
import static com.orm.SugarRecord.listAll;

public class LibraryCourseContentActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private ViewPager viewPager;
    private TabLayout tabLayout;
    ArrayList<Lecture> lectures;
    int course_id;
    Intent intent;
    Bundle bundle;
    String processId;
    Minicourse minicourse;
    LectureListMapping lectureListMapping;
    Tutor t;
    Boolean isEnrolled = false;
    ProgressBar progressBar;
    private TextView titleTextView,followersTextView,descriptionTextView,previousToCurr;
    private RatingBar ratingBar;
    private static String[] tabTitles = {"DETAILS", "LECTURES", "MATERIALS", "REVIEW"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_course_content);


        if(!isConnected()){

                new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                        .setContentText("Connection Error!\nPlease try again later.")
                        .setTitleText("Oops..!!")
                        .show();
            return;
        }

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cordinatorLayout_library_course_content);
        lectures = new ArrayList<>();
        intent = getIntent();
        course_id = intent.getIntExtra("MINICOURSE_ID",0);
        processId = intent.getStringExtra("PROCESS_ID");
        progressBar = findViewById(R.id.pb);
        progressBar.setIndeterminate(true);

        bundle = new Bundle();
        fetchData();

//        Log.d("mini",minicourse.getName());
/*

        intent.putExtra("mini",minicourse);

        intent.putExtra("lol",test);
*/
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        titleTextView = (TextView) findViewById(R.id.LibraryCourseItemTitle);
      //  followersTextView = (TextView) findViewById(R.id.LibraryCourseItemFollowersTextView);
        descriptionTextView = (TextView) findViewById(R.id.LibraryCourseItemDescription);
        previousToCurr = (TextView) findViewById(R.id.previousTOcurrentTextView);
        ratingBar = (RatingBar) findViewById(R.id.LibraryCourseItemRatingbar);

        Typeface typefaceMedium = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Medium.ttf");
        Typeface typefaceRegular = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Regular.ttf");
        titleTextView.setTypeface(typefaceMedium);
      //  followersTextView.setTypeface(typefaceMedium);
        previousToCurr.setTypeface(typefaceMedium);
        descriptionTextView.setTypeface(typefaceRegular);

      //  lectures = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.libraryCourseContentToolbar);
        toolbar.setNavigationIcon(R.drawable.leftarrow);
        setSupportActionBar(toolbar);
        this.setTitle("");

        viewPager = (ViewPager) findViewById(R.id.libraryCourseContentViewPager);


        tabLayout = (TabLayout) findViewById(R.id.libraryCourseContentTabLayout);

        for (int i = 0; i < 4; ++i) {

            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        LibraryCourseContentViewPagerAdapter pagerAdapter = new LibraryCourseContentViewPagerAdapter(getSupportFragmentManager(),bundle);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        final Button enrollbutton = (Button) findViewById(R.id.LibraryCourseItemEnrollButton);


        enrollbutton.setTransformationMethod(null);


        List<MyCoursesEnrolled> enrolledList = listAll(MyCoursesEnrolled.class);

        String toParseCoursesString = "[]";

        if(enrolledList != null && enrolledList.size() > 0){

            toParseCoursesString = enrolledList.get(enrolledList.size() - 1).getMycourses();

        }

        try {

            JSONArray array= new JSONArray(toParseCoursesString);
            for(int i = 0; i < array.length(); ++i){

                int id = array.getInt(i);
                Log.d("tototo",id + " fetched id " + course_id);
                if(id == course_id){

                    isEnrolled = true;
                    break;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(isEnrolled) {

            enrollbutton.setBackgroundResource(R.drawable.enrolled_button_shape);
            enrollbutton.setText("Enrolled");
            enrollbutton.setTextColor(Color.parseColor("#ff1589ee"));
            Snackbar.make(coordinatorLayout, "Already Enrolled", Snackbar.LENGTH_SHORT);
        }

        enrollbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEnrolled){

                    Snackbar.make(coordinatorLayout, "Already Enrolled", Snackbar.LENGTH_SHORT);

                }else {

                    progressBar.setVisibility(View.VISIBLE);

                    List<SessionManager> session = listAll(SessionManager.class);
                    AndroidNetworking.post(Api_Urls.BASE_URL + "api/minicourses/" + course_id + "/enroll")
                            .addHeaders("Authorization", "bearer " + session.get(0).getToken())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    Log.d("lolo", response.toString());

                                    try {
                                        String success = response.getString("success");
                                        if (success.contentEquals("true")) {

                                            enrollbutton.setBackgroundResource(R.drawable.enrolled_button_shape);
                                            enrollbutton.setText("Enrolled");
                                            enrollbutton.setTextColor(Color.parseColor("#ff1589ee"));
                                            isEnrolled = true;

                                            List<MyCoursesEnrolled> enrolledList = listAll(MyCoursesEnrolled.class);

                                            String toParseCoursesString = enrolledList.get(enrolledList.size() - 1).getMycourses();

                                            JSONArray coursesEnrolled = new JSONArray(toParseCoursesString);

                                            coursesEnrolled.put(course_id);

                                            MyCoursesEnrolled enroll = new MyCoursesEnrolled();
                                            enroll.setMycourses(coursesEnrolled.toString());
                                            SugarRecord.save(enroll);


                                            new SweetAlertDialog(LibraryCourseContentActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setContentText("You are enrolled")
                                                    .setTitleText("Enrolled")
                                                    .show();


                                            BaseActivity.getMyCourses();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    } catch (JSONException e) {
                                        new SweetAlertDialog(LibraryCourseContentActivity.this,SweetAlertDialog.ERROR_TYPE)
                                                .setContentText("There seems a problem with us.\nPlease try again later.(101LC_EN)")
                                                .setTitleText("Oops..!!")
                                                .show();
                                    }

                                    updateLccLectureListFragmentAdapter();
                                    progressBar.setVisibility(View.GONE);
                                }
                                @Override
                                public void onError(ANError anError) {

                                    new SweetAlertDialog(LibraryCourseContentActivity.this,SweetAlertDialog.ERROR_TYPE)
                                            .setContentText("Connection Error!\nPlease try again later.(202LC_EN)")
                                            .setTitleText("Oops..!!")
                                            .show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            });


                }

            }
        });

//        final ImageButton bookmarkedButton = (ImageButton) findViewById(R.id.LibraryCourseItemBookMarkButton);
//
//
//        bookmarkedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                bookmarkedButton.setImageResource(R.drawable.bookmarkfilled);
//                Toast.makeText(LibraryCourseContentActivity.this, "Added", Toast.LENGTH_SHORT).show();
//                Snackbar snackbar = Snackbar.make(coordinatorLayout,"Added to your Bookmarks",Snackbar.LENGTH_SHORT);
//                snackbar.show();
//            }
//        });


    }

    public void updateLccLectureListFragmentAdapter(){

        viewPager.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CODE) {

            boolean readingExternal = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean writingExternal = grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (!(readingExternal && writingExternal)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {

                        showMessageOKCancel("You need to give permission to access materials",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                            requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                                        }
                                    }
                                });
                    }
                }
            }
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }


    private void checkData() {

        titleTextView.setText(minicourse.getName());
        descriptionTextView.setText(minicourse.getDescription());
        previousToCurr.setText(minicourse.getSubject() + " > " + minicourse.getCourseName());
        ratingBar.setRating(minicourse.getRating());
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
    }

    private void fetchData() {

        List<SessionManager> session = listAll(SessionManager.class);
        AndroidNetworking.get(Api_Urls.BASE_URL + "api/minicourses/" + course_id)
                .addHeaders("Authorization","bearer" + session.get(0).getToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String medium = response.getString("medium");
                            String difficulty = response.getString("level");
                            String duration = response.getString("duration");

                            JSONArray categories = response.getJSONArray("minicoursecategories");

                            Log.d("toing", String.valueOf(categories));
                            StringBuilder builder = new StringBuilder();

                            for(int i = 0; i < categories.length(); ++i){

                                JSONObject object = categories.getJSONObject(i);
                                JSONObject category = object.getJSONObject("category");



                                builder.append(category.getString("categoryName"));

                            }

                            String relevance = builder.toString();


                            int minicourse_id = response.getInt("id");
                            String minicourse_name = response.getString("name");
                            String description = response.getString("description");
                            float rating = (float) response.getDouble("rating");


                            JSONObject tag = response.getJSONObject("tag");

                            JSONObject subjectObj = tag.getJSONObject("subject");
                            String subject = subjectObj.getString("subjectName");

                            JSONObject courseObj = tag.getJSONObject("course");
                            String course = courseObj.getString("courseName");
                            minicourse = new Minicourse(minicourse_id,minicourse_name,description,difficulty,medium,subject,course,relevance,rating,duration);
                            minicourse.setEnrolled(false);

                            JSONObject tutor = response.getJSONObject("tutor");
                            String teacherName = tutor.getString("name");
                            String teachersdescription = tutor.getString("description");
                            int teachersId = tutor.getInt("id");
                            t = new Tutor(teachersId,teacherName,teachersdescription);

                            JSONArray lessons = response.getJSONArray("lessons");

                            for(int i = 0 ; i < lessons.length(); ++i){

                                JSONObject lesson = lessons.getJSONObject(i);
                                String lesson_name = lesson.getString("name");
                                String url = lesson.getString("videoUrl");
                                String lesson_duration = lesson.getString("duration");
                                int lesson_id = lesson.getInt("id");
                                String lesson_description = lesson.getString("description");
                                int upvotes = lesson.getInt("upvotes");

                                Lecture lecture = new Lecture(lesson_id,lesson_name,url,lesson_duration,lesson_description,upvotes,false,false);
                                lectures.add(lecture);
                            }

                            minicourse.setProcessId(processId);
                            t.setProcessId(processId);
                            LectureListMapping lectureListMapping = new LectureListMapping(processId,lectures);


                          //  SugarRecord.save(minicourse);
                            // SugarRecord.save(t);
                            SugarRecord.save(lectureListMapping);



                            checkData();
                        } catch (JSONException e) {
                            new SweetAlertDialog(LibraryCourseContentActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LC_MI)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        new SweetAlertDialog(LibraryCourseContentActivity.this,SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LC_MI)")
                                .setTitleText("Oops..!!")
                                .show();

                    }
                });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        SugarRecord.deleteAll(Minicourse.class);
        SugarRecord.deleteAll(Tutor.class);
        SugarRecord.deleteAll(LectureListMapping.class);
    }
}



