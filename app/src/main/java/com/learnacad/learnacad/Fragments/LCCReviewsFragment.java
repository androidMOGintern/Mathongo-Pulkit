package com.learnacad.learnacad.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Adapters.ReviewsListViewAdapter;
import com.learnacad.learnacad.Models.MyCoursesEnrolled;
import com.learnacad.learnacad.Models.Reviews;
import com.learnacad.learnacad.Models.SessionManager;
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
 * Created by Sahil Malhotra on 27-08-2017.
 */

public class LCCReviewsFragment extends Fragment {

    @Nullable
    RecyclerView recyclerView;
    TextView ratingTextView,numOfTextView,reviewsHeadingTextView;
    Button rateAndreviewButton,loadMoreButton,rateAndReviewButtonEmptystate;
    int course_id;
    double rating;
    int currPos;
    ListView listView;
    ReviewsListViewAdapter listAdapter;
    float ratings;
    int noOfRatings;
    RatingBar ratingBar;
    ProgressBar progressBar;
    ArrayList<Reviews> reviewsArrayList;
    ArrayList<Reviews> toSendreviewsArrayList;
    RelativeLayout emptyStateLayout;
    NestedScrollView nestedScrollView;
    boolean isReviewed,isEnrolled = false;
    LinearLayout linearLayout;



    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lccratings_reviews_layout,container,false);

        reviewsArrayList = new ArrayList<>();
        toSendreviewsArrayList = new ArrayList<>();
        listView = (ListView) view.findViewById(R.id.reviewsListView);
        loadMoreButton = new Button(getActivity());
        loadMoreButton.setText("Load More");
        loadMoreButton.setBackgroundColor(Color.TRANSPARENT);
        loadMoreButton.setTextColor(Color.parseColor("#1589ee"));
        loadMoreButton.setAllCaps(false);
        emptyStateLayout = (RelativeLayout) view.findViewById(R.id.empty_reviews_state_layoutInclusion);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nested);
        linearLayout = (LinearLayout) view.findViewById(R.id.belowNestedScrllLL);
        rateAndReviewButtonEmptystate = (Button) view.findViewById(R.id.empty_reviews_state_rateandreview_button);
        emptyStateLayout.setVisibility(View.GONE);

        reviewsHeadingTextView = view.findViewById(R.id.reviewsTextView);

        Typeface boldTF = Typeface.DEFAULT_BOLD;
        loadMoreButton.setTypeface(boldTF);
        listView.addFooterView(loadMoreButton);

        progressBar = view.findViewById(R.id.pb);
        progressBar.setIndeterminate(true);
        rateAndreviewButton = (Button) view.findViewById(R.id.lccratings_card_rateandreviewButton);
        ratingBar = (RatingBar) view.findViewById(R.id.lccratings_card_ratingBar);
        ratingTextView = (TextView) view.findViewById(R.id.lccratings_card_ratingValue_TextView);
        numOfTextView = (TextView) view.findViewById(R.id.lccratings_card_numofRatingsValue_TextView);
        rateAndreviewButton = (Button) view.findViewById(R.id.lccratings_card_rateandreviewButton);
        course_id = getActivity().getIntent().getIntExtra("MINICOURSE_ID", 0);

        isEnrolled = checkEnrolled();
        if(isEnrolled){

            rateAndreviewButton.setText("Rate and Review");
            rateAndReviewButtonEmptystate.setText("Rate and Review");
            checkReviewed();


        }else{

            isEnrolled = false;
        }

        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
            }
        });
        fetchData();
        fetchUpperData();

        rateAndReviewButtonEmptystate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isEnrolled || isReviewed){
                    Log.d("isReviewedVal","isReviewed in if" + isReviewed);
                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setConfirmText("OK")
                            .setContentText("It seems that you had already reviewed the course or have not enrolled to it.");

                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.dismissWithAnimation();
                        }
                    });

                    dialog.setCancelable(true);
                    dialog.show();
                    return;

                }else {
                    Log.d("isReviewedVal","isReviewed in else" + isReviewed);

                    rateAndreviewClick();
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        rateAndreviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isEnrolled || isReviewed){
                    Log.d("isReviewedVal","isReviewed in if" + isReviewed);
                    final SweetAlertDialog dialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setConfirmText("OK")
                            .setContentText("It seems that you had already reviewed the course or have not enrolled to it.");

                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.dismissWithAnimation();
                        }
                    });

                    dialog.setCancelable(true);
                    dialog.show();
                    return;

                }else {
                    Log.d("isReviewedVal","isReviewed in else" + isReviewed);

                    rateAndreviewClick();
                }
            }
        });

        listAdapter = new ReviewsListViewAdapter(getActivity(),toSendreviewsArrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflator = getActivity().getLayoutInflater();
                final View dialogView = inflator.inflate(R.layout.detailed_review_layout, null);
                builder.setView(dialogView);
                TextView nameTextView = (TextView) dialogView.findViewById(R.id.detailedReviewLayoutNameofStudent);
                ImageButton closeButton = (ImageButton) dialogView.findViewById(R.id.imageButtonReviewDialog);
                TextView descTextView = (TextView) dialogView.findViewById(R.id.TextViewReviewDialog);
                RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBardetailedReviewLayout);

                nameTextView.setText(toSendreviewsArrayList.get(i).getStudentName());

                if(toSendreviewsArrayList.get(i).getDescription().length() > 0 && toSendreviewsArrayList.get(i).getDescription().isEmpty()){

                    descTextView.setVisibility(View.GONE);
                }else {
                    descTextView.setText(toSendreviewsArrayList.get(i).getDescription());
                }

                    ratingBar.setRating(toSendreviewsArrayList.get(i).getRating());

                final AlertDialog dialog = builder.create();
                dialog.show();

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });


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
                    .setContentText("There seems a problem with us.\nPlease try again later.(101LC_RF_CE)")
                    .setTitleText("Oops..!!")
                    .show();
        }

        return false;

    }


    public void rateAndreviewClick(){

        FlurryAgent.logEvent("Course_"+ course_id + "_Reviewed");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflator = getActivity().getLayoutInflater();
        final View dialogView = inflator.inflate(R.layout.review_post_layout, null);
        builder.setView(dialogView);
        final EditText errorEditText = (EditText) dialogView.findViewById(R.id.editTextReviewDialog);
        Button submitButton = (Button) dialogView.findViewById(R.id.submitButtonReviewDialog);
        ImageButton closeButton = (ImageButton) dialogView.findViewById(R.id.imageButtonReviewDialog);
        final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingBarReviewPostItem);




        final AlertDialog dialog = builder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!isConnected()){

                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Connection Error!\nPlease try again later.")
                            .setTitleText("Oops..!!")
                            .show();
                    return;
                }

                dialog.cancel();

                ratings = ratingBar.getRating();

                if(ratings == 0){
                     ratings = 1;
                }

                List<SessionManager> sessionManagers = listAll(SessionManager.class);

                progressBar.setVisibility(View.VISIBLE);

                String review = errorEditText.getText().toString().trim();

                if(review.isEmpty()){

                    review = "";
                }


                AndroidNetworking.post(Api_Urls.BASE_URL + "api/reviews/" + course_id)
                        .addHeaders("Authorization", "Bearer " + sessionManagers.get(0).getToken())
                        .addUrlEncodeFormBodyParameter("description", String.valueOf(review))
                        .addUrlEncodeFormBodyParameter("rating",String.valueOf(ratings))
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    String success = response.getString("success");
                                    if (success.contentEquals("true")) {

                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Submitted Successfully!")
                                                .setContentText("Thank you for your feedback.")
                                                .show();

                                        isReviewed = true;
                                    }

                                } catch (JSONException e) {

                                    progressBar.setVisibility(View.GONE);

                                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                            .setContentText("There seems a problem with us.\nPlease try again later.(101LC_RF_RP)")
                                            .setTitleText("Oops..!!")
                                            .show();
                                }

                                progressBar.setVisibility(View.GONE);


                                reviewsArrayList.clear();
                                fetchData();
                                fetchUpperData();
                            }

                            @Override
                            public void onError(ANError anError) {
                                new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Connection Error!\nPlease try again later.(202LC_RF_RP)")
                                        .setTitleText("Oops..!!")
                                        .show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });


            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });


    }

    private void fetchUpperData() {



        List<SessionManager> session = listAll(SessionManager.class);
        AndroidNetworking.get(Api_Urls.BASE_URL + "api/minicourses/" + course_id)
                .addHeaders("Authorization","bearer" + session.get(0).getToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            rating = response.getDouble("rating");
                            noOfRatings = response.getInt("noOfRatings");

                        } catch (JSONException e) {
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LC_RF_UD)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }
                        setUpperData(rating,noOfRatings);
                    }

                    @Override
                    public void onError(ANError anError) {

                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LC_RF_UD)")
                                .setTitleText("Oops..!!")
                                .show();

                    }
                });


    }

    private void setUpperData(double r,int noofR) {

        if(noofR == 0){

            emptyStateLayout.setVisibility(View.VISIBLE);
            nestedScrollView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);

        }else{

            emptyStateLayout.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
        }
        ratingTextView.setText(String.valueOf((float) round(r,1)));
        numOfTextView.setText(String.valueOf(noofR) + " Ratings");
        ratingBar.setRating((float) round(r,1));
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#ffb75d"), PorterDuff.Mode.SRC_ATOP);
    }

    private static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private void fetchData() {


        List<SessionManager> sessionManagers = listAll(SessionManager.class);

        reviewsArrayList.clear();

        progressBar.setVisibility(View.VISIBLE);
        AndroidNetworking.get(Api_Urls.BASE_URL + "api/minicourses/" + course_id)
                .addHeaders("Authorization","bearer " + sessionManagers.get(0).getToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("0p0p",response.toString());

                        try {
                            JSONArray reviews = response.getJSONArray("reviews");

                            for(int i = reviews.length() - 1; i >= 0; --i){

                                JSONObject object = reviews.getJSONObject(i);
                                String temp = object.getString("rating");
                                if(temp.contentEquals("null")){

                                    rating = 0;

                                }else{

                                    rating = Integer.parseInt(temp);
                                }

                                String description = object.getString("description");
                                JSONObject student = object.getJSONObject("student");
                                String studentName = student.getString("name");
                                int studentId = student.getInt("id");
                                if(!TextUtils.isEmpty(description)){

                                    reviewsArrayList.add(new Reviews((int) rating, description, studentId, studentName));
                                }
                            }
                        } catch (JSONException e) {


                        }

                        progressBar.setVisibility(View.GONE);
                        toSendreviewsArrayList.clear();
                        currPos = 0;
                        setData();
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("0p0p",anError.getErrorBody());
                        Log.d("0p0p",anError.getErrorDetail());
                        Log.d("0p0p",anError.getErrorCode() + "");
                        Log.d("0p0p",anError.getLocalizedMessage());

                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LC_RF_LD)\n")
                                .setTitleText("Oops..!!")
                                .show();

                        progressBar.setVisibility(View.GONE);


                    }
                });



    }


    private void setData() {


        if (reviewsArrayList.size() == 0) {

            reviewsHeadingTextView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            loadMoreButton.setVisibility(View.GONE);

        } else {

            reviewsHeadingTextView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);
            loadMoreButton.setVisibility(View.VISIBLE);

            if (currPos < 0) {

                loadMoreButton.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "No more reviews to show", Toast.LENGTH_SHORT).show();
                return;
            }


            int count;
            count = currPos + 5;

            for (int i = currPos; i < count && i < reviewsArrayList.size(); ++i) {

                toSendreviewsArrayList.add(reviewsArrayList.get(i));
            }

            if (count > reviewsArrayList.size()) {

                currPos = -100;
            } else {
                currPos = count;

            }
//            adapter.setLoaded();

            listAdapter.notifyDataSetChanged();
            int currentPos = listView.getFirstVisiblePosition();
            listView.setSelectionFromTop(currentPos, 0);
//            adapter.notifyDataSetChanged();
        }
    }

    private void checkReviewed(){

        List<SessionManager> sessions = listAll(SessionManager.class);

        AndroidNetworking.get(Api_Urls.BASE_URL + "api/reviews/isReviewed/" + course_id)
                .addHeaders("Authorization","Bearer " + sessions.get(0).getToken())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String result = response.getString("isReviewed");

                            if(result.contentEquals("true")){

                                isReviewed = true;

                                rateAndreviewButton.setBackgroundResource(R.drawable.enrolled_button_library_shape);
                                rateAndreviewButton.setText("Course Rated");
                            }
                        } catch (JSONException e) {

                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LC_RF_CR)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LC_RF_CR)")
                                .setTitleText("Oops..!!")
                                .show();

                    }
                });

    }


    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }


}
