package com.learnacad.learnacad.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.flurry.android.FlurryAgent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.learnacad.learnacad.Fragments.Bookmarks_Fragment;
import com.learnacad.learnacad.Fragments.LibraryCourseListFragment;
import com.learnacad.learnacad.Fragments.Library_Fragment;
import com.learnacad.learnacad.Fragments.MyCourses_Fragment;
import com.learnacad.learnacad.Fragments.NoInternetConnectionFragment;
import com.learnacad.learnacad.Fragments.Resources_Fragments.ResourcesBaseFragment;
import com.learnacad.learnacad.Models.MyCoursesEnrolled;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.Student;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.orm.SugarRecord.SUGAR;
import static com.orm.SugarRecord.listAll;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;
    private  NavigationView navigationView;
    public static MyCoursesEnrolled coursesEnrolled;
    boolean doubleBackPressToExitPressedOnce = false;
    private FirebaseAnalytics firebaseAnalytics;
    Student student;
    String myReferalCode;
    public ActionBarDrawerToggle toggle;
    String version;
    HashMap<String,String> flurryMaps;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        SugarRecord.deleteAll(MyCoursesEnrolled.class);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);

        flurryMaps = new HashMap<>();


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //checkForUpdate();

      //  Toolbar bottomToolbar = (Toolbar) findViewById(R.id.toolbarBottom);

        List<Student> students = SugarRecord.listAll(Student.class);
        if(students != null && students.size() > 0)
        student = students.get(0);

        flurryMaps.put("name_of_student",student.getName());
        flurryMaps.put("class_or_target_year_of_student",student.getClassChoosen());



        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        if(isConnected()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new Library_Fragment(),"LibraryShown");
            fragmentTransaction.commit();


        }else{

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, new ResourcesBaseFragment());
            fragmentTransaction.commit();
        }


        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        final RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.splashRelativeLayout);

//        drawer.setDrawerShadow();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView nameTextViewNavDrawer = (TextView) navigationView.getHeaderView(0).findViewById(R.id.textViewNameNavigationDrawer);

        String name = student.getName();
        nameTextViewNavDrawer.setText(name + " ");

        getMyCourses();

    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            for(int i = 0; i < navigationView.getMenu().size(); ++i){

                navigationView.getMenu().getItem(i).setChecked(false);
            }
        } else {

            FragmentManager fm = getSupportFragmentManager();


            if (LibraryCourseListFragment.ISVISIBLE) {

                for (int i = 0; i < navigationView.getMenu().size(); ++i) {

                    navigationView.getMenu().getItem(i).setChecked(false);
                }

                if (doubleBackPressToExitPressedOnce) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    return;
                }
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                this.doubleBackPressToExitPressedOnce = true;


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        doubleBackPressToExitPressedOnce = false;
                    }
                }, 2000);
            } else {

                super.onBackPressed();

            }
        }

    }




    public static void getMyCourses(){



        final ArrayList<Integer> enrolledCourses = new ArrayList<>();



        List<SessionManager> sessions = listAll(SessionManager.class);

        AndroidNetworking.get(Api_Urls.BASE_URL + "api/students/mycourses")
                .addHeaders("Authorization","Bearer " + sessions.get(0).getToken() )
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for(int i = 0; i < response.length(); ++i){

                            try {
                                JSONObject object = response.getJSONObject(i);

                                int idToAdd = object.getInt("minicourseId");

                                if(!enrolledCourses.contains(idToAdd)){

                                    enrolledCourses.add(idToAdd);
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        JSONArray courses = new JSONArray();


                        for(int i = 0; i < enrolledCourses.size(); ++i){

                            courses.put(enrolledCourses.get(i));
                        }

                        coursesEnrolled = new MyCoursesEnrolled();
                        coursesEnrolled.setMycourses(courses.toString());

                        SugarRecord.save(coursesEnrolled);

                        Log.d("789456123","Inside getMyCourse done");


                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        for(int i = 0; i < navigationView.getMenu().size(); ++i){

                navigationView.getMenu().getItem(i).setChecked(false);
        }

        item.setChecked(true);

        switch (id){

            case R.id.libraryNavigationDrawer:{

                if(!isConnected()){

                    new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Connection Error!\nPlease try again later.")
                            .setTitleText("Oops..!!")
                            .show();
                            return true;
                }

                FlurryAgent.logEvent("Library_NavClicked");

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame,new LibraryCourseListFragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
            break;



            case R.id.mybookmarksNavigationDrawer:{


                if(!isConnected()){

                    new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Connection Error!\nPlease try again later.")
                            .setTitleText("Oops..!!")
                            .show();
                    return true;
                }

                FlurryAgent.logEvent("MyBookMarks_NavClicked");


                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new Bookmarks_Fragment());
                ft.addToBackStack(null).commit();
            }
            break;

            case R.id.resourcesNavigationDrawer:{

                FlurryAgent.logEvent("Resources_NavClicked");



                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new ResourcesBaseFragment());
                ft.addToBackStack(null).commit();
            }
            break;

            case R.id.myCoursesNavigationDrawer:{

                if(!isConnected()){

                    new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Connection Error!\nPlease try again later.")
                            .setTitleText("Oops..!!")
                            .show();
                    return true;
                }

                FlurryAgent.logEvent("MyCourses_NavClicked");


                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new MyCourses_Fragment());
                ft.addToBackStack(null).commit();
            }
            break;

            case R.id.logoutNavigationDrawer:{

                if(!isConnected()){

                    new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Connection Error!\nPlease try again later.")
                            .setTitleText("Oops..!!")
                            .show();
                    return true;
                }

                FlurryAgent.logEvent("Logout_NavClicked");



                final SweetAlertDialog dialog =  new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                LogoutAsyncTask logoutTask = new LogoutAsyncTask();
                                logoutTask.execute(Api_Urls.BASE_URL);
                            }
                        })
                       .setCancelText("Cancel");

                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                           @Override
                           public void onClick(SweetAlertDialog sweetAlertDialog) {
                               dialog.dismissWithAnimation();
                           }
                       });

                dialog.setCancelable(true);
                dialog.show();


            }
            break;

            default:{

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // function to push the generated referal code of the student to db

    private void pushReferalCode() {

        AndroidNetworking.post(Api_Urls.BASE_URL + "authorize/addrefercode")
                .addUrlEncodeFormBodyParameter("studentId", String.valueOf(student.getStudentId()))
                .addUrlEncodeFormBodyParameter("refercode",myReferalCode)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String success = response.getString("success");
                            if(success.contentEquals("true")){


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // TODO show the apt error
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        // TODO show the apt error
                    }
                });


    }



    private void generateReferalCode() {

        StringBuilder sb = new StringBuilder();

        String name = student.getName();

        name = name.toUpperCase();

        for(int i = 0; i < name.length(); ++i){

            if(!(name.charAt(i) == ' ')){

                sb.append(name.charAt(i));
            }
        }

        name = sb.toString();

        StringBuilder referalCodeBuilder = new StringBuilder();
        referalCodeBuilder.append(name.charAt(0));
        referalCodeBuilder.append(name.charAt(1));
        referalCodeBuilder.append(name.charAt(2));

        int code = randomWithRange(1000,9999);

        referalCodeBuilder.append(code);

        myReferalCode = referalCodeBuilder.toString();
        student.setMyReferalCode(myReferalCode);
        pushReferalCode();

    }

    private void logout(boolean message) {

        Log.d("tutu","Logout functin called " + message);

        if(message) {
            SugarRecord.deleteAll(SessionManager.class);
            SugarRecord.deleteAll(Student.class);
            Intent intent = new Intent(this, SplashActivity.class);
            intent.putExtra("SPLASH_DONE",true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{

            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Connection Error!\nPlease try again later.")
                    .setTitleText("Oops..!!")
                    .show();

        }
    }

    private void checkisReferalSet() {

        int sId = student.getStudentId();

        AndroidNetworking.post(Api_Urls.BASE_URL + "authorize/referexists/" + sId)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String success;
                        try {

                          success = response.getString("success");
                            if(success.contentEquals("Refer Not Exists")) {

                                generateReferalCode();
                            }

                          Log.d("checkRefer",success);

                        } catch (JSONException e) {

                            // TODO show an apt error
                            e.printStackTrace();

                        }


                    }

                    @Override
                    public void onError(ANError anError) {


                        // TODO show an apt error

                        Log.d("opopop",anError.toString());
                        Log.d("opopop",anError.getErrorBody());
                        Log.d("opopop", String.valueOf(anError.getErrorCode()));
                        Log.d("opopop",anError.getErrorDetail());

                    }
                });
    }


    public int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }


    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));


    }



    public class LogoutAsyncTask extends AsyncTask<String,Void,String>  {


        @Override
        protected String doInBackground(String... params) {
           StringBuffer sb = new StringBuffer();

            try {
                URL url = new URL(Api_Urls.BASE_URL + "logout");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                if(inputStream == null){
                    return null;
                }

                Scanner sc = new Scanner(inputStream);

                while(sc.hasNext()){

                    sb.append(sc.nextLine());
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return sb.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

                Log.d("tutu",s);

            try {
                JSONObject obj = new JSONObject(s);
                String success = obj.getString("success");
                Log.d("tutu","success = " + success);
                if(success.contentEquals("true")){

                    logout(true);
                }else{

                    logout(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
