package com.learnacad.learnacad.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.flurry.android.FlurryAgent;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.learnacad.learnacad.Adapters.LecturePlayerViewPagerAdapter;
import com.learnacad.learnacad.Models.Lecture;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.Tutor;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.orm.SugarRecord.listAll;

public class LecturePlayerActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

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

    public static final String GOOGLE_DEVELOPER_KEY = "AIzaSyBrJtaqoS-xR6LdGTdlSHJm7pp8pBTGrEE";
    public static String YOUTUBE_CODE;
    YouTubePlayer player;
    ViewPager viewPager;
    TabLayout tabLayout;
    Tutor tutor;
    Button upVoteButton;
    Button bookmarkButton;
    int currPosition;
    ProgressBar progressBar;
    ArrayList<Lecture> lectures;
    static TextView aTitletextView,aDescptextView,teachersNametextView,teachersDesctextView,adurationTextView;
    private String[] tabTitles = {"LECTURES", "COMMENTS","DOUBTS"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture);
        progressBar = findViewById(R.id.pb);
        progressBar.setIndeterminate(true);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        if(!isConnected()){

            new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Connection Error!\nPlease try again later.")
                    .setTitleText("Oops..!!")
                    .show();
            return;
        }

        lectures = new ArrayList<>();
        Intent intent = getIntent();
        Lecture selectedLecture = (Lecture) intent.getSerializableExtra("selectedLecture");
    //    tutor = (Tutor) intent.getSerializableExtra("selectedTutor");
        final int position = intent.getIntExtra("selectedPosition",0);
        lectures = (ArrayList<Lecture>) intent.getSerializableExtra("lectureList");


        aTitletextView = (TextView) findViewById(R.id.attachedLayoutTitleTextView);
        aDescptextView = (TextView) findViewById(R.id.attachedLayoutDescriptionTextView);
        adurationTextView = (TextView) findViewById(R.id.attachedLayoutDurationTextView);
        upVoteButton = (Button) findViewById(R.id.attachedUpvotesButton);
        bookmarkButton = (Button) findViewById(R.id.attachedBookmarkButton);
//        teachersNametextView = (TextView) findViewById(R.id.lecturePlayerTeacherNameTextView);
//        teachersDesctextView = (TextView) findViewById(R.id.lecturePlayerTeacherDescriptionTextView);
//
//        teachersNametextView.setText(tutor.getName());
//        teachersDesctextView.setText(tutor.getDescription());

        setDataLecturePlayer(position);

        setVideoCode(selectedLecture.getUrl());

        viewPager = (ViewPager) findViewById(R.id.lecturePlayerViewPager);
        tabLayout = (TabLayout) findViewById(R.id.lecturePlayerTabLayout);

        for(int i = 0; i < 1; ++i){

            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]));
        }

        LecturePlayerViewPagerAdapter viewPagerAdapter = new LecturePlayerViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
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

        YouTubePlayerSupportFragment youTubePlayerSupportFragment = (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.youtubePlayer);
        youTubePlayerSupportFragment.initialize(GOOGLE_DEVELOPER_KEY,this);

        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(Color.BLACK);
        drawable.setSize(2,1);
        drawable.setShape(GradientDrawable.LINE);
        linearLayout.setDividerPadding(10);
        linearLayout.setDividerDrawable(drawable);



//        final Button shareButton = (Button) findViewById(R.id.attachedshareButton);
//        final Button downloadsButton = (Button) findViewById(R.id.attachedDownloadsButton);
        final Button reportButton = (Button) findViewById(R.id.attachedUReportButton);


        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LecturePlayerActivity.this);
                LayoutInflater inflator = LecturePlayerActivity.this.getLayoutInflater();
                final View dialogView = inflator.inflate(R.layout.report_error_dialog_layout,null);
                builder.setView(dialogView);
                final EditText errorEditText = (EditText) dialogView.findViewById(R.id.editTextReportErrorDialog);
                Button submitButton = (Button) dialogView.findViewById(R.id.submitButtonReportErrorDialog);
                ImageButton closeButton = (ImageButton) dialogView.findViewById(R.id.imageButtonReportErrorDialog);


                final AlertDialog dialog = builder.create();
                dialog.show();


                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FlurryAgent.logEvent("Report_"+ lectures.get(position).getName() + "Clicked");


                        dialog.cancel();

                            final int lesson_id = lectures.get(currPosition).getLecture_id();
                            List<SessionManager> sessionManagers = listAll(SessionManager.class);
                        progressBar.setVisibility(View.VISIBLE);

                        String reportError = errorEditText.toString().trim();

                            if(reportError.isEmpty()) {


                                AndroidNetworking.post(Api_Urls.BASE_URL + "api/lessons/" + lesson_id + "/report")
                                        .addHeaders("Authorization", "Bearer " + sessionManagers.get(0).getToken())
                                        .addUrlEncodeFormBodyParameter("description",reportError)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {

                                                try {
                                                    String success = response.getString("success");
                                                    if (success.contentEquals("true")) {

                                                        new SweetAlertDialog(LecturePlayerActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                                .setTitleText("Reported Successfully!")
                                                                .setContentText("Thank you for improving us.")
                                                                .show();
                                                    }

                                                } catch (JSONException e) {
                                                    new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                                            .setContentText("There seems a problem with us.\nPlease try again later.(101LP_RE)")
                                                            .setTitleText("Oops..!!")
                                                            .show();
                                                }

                                                progressBar.setVisibility(View.GONE);
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                                        .setContentText("Connection Error!\nPlease try again later.(202LP_RE)")
                                                        .setTitleText("Oops..!!")
                                                        .show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                            }else{


                                new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Sorry nothing to be submitted.")
                                        .setTitleText("Oops...")
                                        .show();

                                progressBar.setVisibility(View.GONE);
                            }
                        progressBar.setVisibility(View.GONE);
                    }
                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.cancel();
                    }
                });
            }
        });


/*        final Button followButton = (Button) findViewById(R.id.lecturePlayerTeacherFollowButton);

        followButton.setTransformationMethod(null);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                followButton.setBackgroundResource(R.drawable.following_button_shape);
                followButton.setText("Following");
                followButton.setTextColor(Color.parseColor("#ffffffff"));
            }
        });*/

        upVoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FlurryAgent.logEvent("Upvote_"+ lectures.get(position).getName() + "Clicked");

                if(!lectures.get(currPosition).isUpVoted()) {

                    final int lesson_id = lectures.get(currPosition).getLecture_id();
                    List<SessionManager> sessionManagers = listAll(SessionManager.class);
//                    showDialog();
//                    pDialog.setMessage("Loading...");

                    AndroidNetworking.post(Api_Urls.BASE_URL + "api/lessons/" + lesson_id + "/upvote")
                            .addHeaders("Authorization", "Bearer " + sessionManagers.get(0).getToken())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String success = response.getString("success");
                                        if (success.contentEquals("true")) {

                                            lectures.get(currPosition).setUpVoted(true);
                                            upVoteButton.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.new_upvoteactive_layer_list_drawable, 0, 0);
                                            int numOfUpVotes = lectures.get(currPosition).getUpvotes();
                                            ++numOfUpVotes;
                                            lectures.get(currPosition).setUpvotes(numOfUpVotes);
                                            upVoteButton.setText(numOfUpVotes + " Upvotes");
                                        }

                                    } catch (JSONException e) {
                                        new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                                .setContentText("There seems a problem with us.\nPlease try again later.(101LP_UP)")
                                                .setTitleText("Oops..!!")
                                                .show();
                                    }


                                }

                                @Override
                                public void onError(ANError anError) {
                                    new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                            .setContentText("Connection Error!\nPlease try again later.(202LP_UP)")
                                            .setTitleText("Oops..!!")
                                            .show();
                                }
                            });
                }
            }
        });

        bookmarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!lectures.get(currPosition).isBookmarked()) {

                    final int lesson_id = lectures.get(currPosition).getLecture_id();
                    List<SessionManager> sessionManagers = listAll(SessionManager.class);
                    progressBar.setVisibility(View.VISIBLE);

                    AndroidNetworking.post(Api_Urls.BASE_URL + "api/lessons/" + lesson_id + "/bookmark")
                            .addHeaders("Authorization", "Bearer " + sessionManagers.get(0).getToken())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    try {
                                        String success = response.getString("success");
                                        if (success.contentEquals("true")) {

                                            lectures.get(currPosition).setBookmarked(true);
                                            bookmarkButton.setBackgroundResource(R.drawable.enrolled_button_library_shape);
                                            bookmarkButton.setText("Bookmarked");

                                        }

                                    } catch (JSONException e) {
                                        new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                                .setContentText("There seems a problem with us.\nPlease try again later.(101LP_BO)")
                                                .setTitleText("Oops..!!")
                                                .show();
                                    }

                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError(ANError anError) {
                                    new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                            .setContentText("Connection Error!\nPlease try again later.(202LP_BO)")
                                            .setTitleText("Oops..!!")
                                            .show();
                                    progressBar.setVisibility(View.GONE);

                                }
                            });
                }
            }
        });


//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                shareButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.shareactive,0,0);
//            }
//        });
//
//        downloadsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                downloadsButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.downloadiconactive,0,0);
//                downloadsButton.setText("Downloaded");
//
//            }
//        });
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }

    private void setVideoCode(String url) {

      int slashIndex = url.lastIndexOf("/");


        Log.d("youtubelink",url);
        StringBuilder builder = new StringBuilder();
        builder.append(url,slashIndex + 1,url.lastIndexOf("?"));
        Log.d("youtubelink",builder.toString());
        YOUTUBE_CODE = builder.toString();
    }

    public void setDataLecturePlayer(int postion){

            currPosition = postion;

            aTitletextView.setText(lectures.get(postion).getName());
            aDescptextView.setText(lectures.get(postion).getDescription());
            adurationTextView.setText(lectures.get(postion).getDuration());

                int numOfUpVotes = lectures.get(postion).getUpvotes();
            if(numOfUpVotes > 0){

                upVoteButton.setText(numOfUpVotes + " Upvotes");
            }else{

                upVoteButton.setText("0 Upvotes");
            }
            setVideoCode(lectures.get(postion).getUrl());



        if(lectures.get(postion).isUpVoted()){
            upVoteButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.new_upvoteactive_layer_list_drawable,0,0);
        }else{

            checkUpvoted(postion);
        }

        if(lectures.get(postion).isBookmarked()){
            bookmarkButton.setBackgroundResource(R.drawable.enrolled_button_library_shape);
            bookmarkButton.setText("Bookmarked");

        }else{

            checkBookmarked(postion);
        }

    }

    private void checkBookmarked(final int postion) {

        int lesson_id = lectures.get(postion).getLecture_id();
        List<SessionManager> sessionManagers = listAll(SessionManager.class);

        AndroidNetworking.get(Api_Urls.BASE_URL + "api/lessons/" + lesson_id + "/isBookmarked")
                .addHeaders("Authorization","Bearer " + sessionManagers.get(0).getToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String success = response.getString("isBookmarked");
                            if(success.contentEquals("true")){

                                lectures.get(postion).setBookmarked(true);
                                bookmarkButton.setBackgroundResource(R.drawable.enrolled_button_library_shape);
                                bookmarkButton.setText("Bookmarked");
                            }

                        } catch (JSONException e) {
                            new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LP_IB)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LP_IB)")
                                .setTitleText("Oops..!!")
                                .show();
                    }
                });
    }

    public void newDataLectureClicked(int position){

        Intent intent = new Intent(this, LecturePlayerActivity.class);
        intent.putExtra("selectedLecture",lectures.get(position));
        intent.putExtra("selectedPosition",position);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra("lectureList",lectures);
        startActivity(intent);
        this.finish();
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        player = youTubePlayer;

        if(!b){

            player.loadVideo(YOUTUBE_CODE);
        }

    }



    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        final int REQUEST_CODE = 1;

        if(youTubeInitializationResult.isUserRecoverableError()){

            youTubeInitializationResult.getErrorDialog(this,REQUEST_CODE);
        }else{

            new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                    .setContentText("Connection Error!\nPlease try again later.")
                    .setTitleText("Oops..!!")
                    .show();
        }
    }


    private void checkUpvoted(final int pos){

        int lesson_id = lectures.get(pos).getLecture_id();
        List<SessionManager> sessionManagers = listAll(SessionManager.class);

        AndroidNetworking.get(Api_Urls.BASE_URL + "api/lessons/" + lesson_id + "/isUpvoted")
                .addHeaders("Authorization","Bearer " + sessionManagers.get(0).getToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                           String success = response.getString("success");
                            if(success.contentEquals("true")){

                                lectures.get(pos).setUpVoted(true);
                                upVoteButton.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.new_upvoteactive_layer_list_drawable,0,0);
                                int numOfUpVotes = lectures.get(currPosition).getUpvotes();
                                ++numOfUpVotes;
                                lectures.get(currPosition).setUpvotes(numOfUpVotes);
                                upVoteButton.setText(numOfUpVotes + " Upvotes");
                            }

                        } catch (JSONException e) {
                            new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LP_IU)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        new SweetAlertDialog(LecturePlayerActivity.this,SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LP_IU)")
                                .setTitleText("Oops..!!")
                                .show();
                    }
                });
    }

}
