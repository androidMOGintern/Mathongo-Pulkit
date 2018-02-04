package com.learnacad.learnacad.Fragments;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.BaseActivity;
import com.learnacad.learnacad.Adapters.ChipsViewAdapeter;
import com.learnacad.learnacad.Adapters.LibraryCourseListAdapter;
import com.learnacad.learnacad.BuildConfig;
import com.learnacad.learnacad.Fragments.Resources_Fragments.ResourcesBaseFragment;
import com.learnacad.learnacad.Models.CONSTANTS;
import com.learnacad.learnacad.Models.Filter;
import com.learnacad.learnacad.Models.FiltersViewModel;
import com.learnacad.learnacad.Models.Minicourse;
import com.learnacad.learnacad.Models.SessionManager;
import com.learnacad.learnacad.Models.Tutor;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.orm.SugarRecord.deleteAll;
import static com.orm.SugarRecord.listAll;
import static java.lang.Boolean.TRUE;

/**
 * Created by Sahil Malhotra on 19-06-2017.
 */

public class LibraryCourseListFragment extends Fragment implements ChipsViewAdapeter.onChipDeleted, AppBarLayout.OnOffsetChangedListener {

    RecyclerView recyclerView;
    LibraryCourseListAdapter listAdapter;
    ChipsViewAdapeter chipsViewAdapeter;
    ArrayList<Minicourse> minicoursesList;
    ArrayList<Tutor> tutors;
    ArrayList<String> chipsTitles;
    private static View view;
    TextView filterTextview;
    CardView chipsCardView;
    RecyclerView chipRecyclerView;
    Filter f;
    Bundle b;
    Context mContext;
    SearchView searchView;
    FiltersViewModel mViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    AppBarLayout appBarLayout;
    ImageView imageViewHeader;
    String version;

    public static boolean ISVISIBLE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if(view != null){

            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){

                parent.removeAllViews();
            }
        }

        ISVISIBLE = true;

        try {

            view = inflater.inflate(R.layout.courses_list_library_fragment_layout,container,false);
        }catch (InflateException e){

        }


        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(getActivity()).get(FiltersViewModel.class);


        appBarLayout = view.findViewById(R.id.BaseActivityAppBarLayout);

        recyclerView = (RecyclerView) view.findViewById(R.id.LibraryCourseListRecyclerView);
        minicoursesList = new ArrayList<>();
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        tutors = new ArrayList<>();
        listAdapter = new LibraryCourseListAdapter(getActivity(),minicoursesList,swipeRefreshLayout,tutors);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.pb);
        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);

        getActivity().setTitle("Library");
        BaseActivity.getMyCourses();

        chipsTitles = new ArrayList<>();

        filterTextview = (TextView) view.findViewById(R.id.filterTextView);
         chipRecyclerView = (RecyclerView) view.findViewById(R.id.internalLinearLayout);
        chipsCardView = (CardView) view.findViewById(R.id.cardView1);
        chipsViewAdapeter = new ChipsViewAdapeter(getActivity(),chipsTitles,chipsCardView,this);
        chipRecyclerView.setAdapter(chipsViewAdapeter);

        imageViewHeader = view.findViewById(R.id.headerImageView);


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        chipRecyclerView.setLayoutManager(layoutManager);


        if(!isConnected()){

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame,new NoInternetConnectionFragment());
            fragmentTransaction.commit();
        }

        chipsCardView.setVisibility(View.GONE);



        b = getArguments();
        setData(b);

        return view;
    }



    @Override
    public void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    private void setData(Bundle b) {

        if(b !=  null){


            final ArrayList<String> checkedItems = b.getStringArrayList("checkedItems");

            fetchDataWithFilters(checkedItems);

            if(checkedItems != null && checkedItems.size() > 0) {

                chipsTitles.clear();
                chipsTitles.addAll(checkedItems);
                chipsViewAdapeter.notifyDataSetChanged();
                chipsCardView.setVisibility(View.VISIBLE);

            }

        }else{

            fetchData();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_library,menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                listAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                listAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.notificationsFilters){

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Filters filters = new Filters();
            Bundle alreadyCheckedItemsBundle = new Bundle();
            alreadyCheckedItemsBundle.putStringArrayList("alreadyChecked",chipsTitles);
            filters.setArguments(alreadyCheckedItemsBundle);
            ft.replace(R.id.content_frame, filters);
            ft.addToBackStack(null).commit();

        }

        if(item.getItemId() == R.id.action_search){

            return true;

       }

//        if(item.getItemId() == R.id.NotificationsIII){
//
//            startActivity(new Intent(getActivity(), NotificationsActivity.class));
//        }

        return true;
    }








    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                minicoursesList.clear();
                tutors.clear();

                listAdapter.notifyDataSetChanged();

                if(chipsCardView.getVisibility() == View.VISIBLE) {

                    chipsCardView.setVisibility(View.GONE);
                    fetchData();

                }else{

                    fetchData();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        imageViewHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FlurryAgent.logEvent("Resources_Banner_Clicked");


                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, new ResourcesBaseFragment());
                fragmentTransaction.addToBackStack(null).commit();
            }
        });

    }


    private void fetchData() {

        minicoursesList.clear();
        tutors.clear();
        mViewModel.clearall();
        deleteAll(Minicourse.class);
        deleteAll(Tutor.class);

        progressBar.setVisibility(View.VISIBLE);

        List<SessionManager> session = listAll(SessionManager.class);
        AndroidNetworking.get(Api_Urls.BASE_URL + "api/minicourses")
                .addHeaders("Authorization","Bearer " + session.get(0).getToken())
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            for(int i = 0; i < response.length(); ++i){

                                JSONObject object = response.getJSONObject(i);

                                String isActive = object.getString("isActive");

                                if(!isActive.contentEquals("true")){

                                    continue;
                                }

                                int minicourse_id = object.getInt("id");
                                String minicourse_name = object.getString("name");

                                String minicourse_description = object.getString("description");
                                String minicourse_rating = object.getString("rating");



                                Minicourse minicourse = new Minicourse();
                                minicourse.setCourse_id(minicourse_id);
                                minicourse.setName(minicourse_name);
                                minicourse.setDescription(minicourse_description);
                                minicourse.setRating(Float.parseFloat(minicourse_rating));
                                StringBuilder builder = new StringBuilder();


                                JSONArray categories = object.getJSONArray("minicoursecategories");
                                for(int j = 0; j < categories.length(); ++j){

                                    JSONObject catObject = categories.getJSONObject(j);
                                    JSONObject lastObjtoCat = catObject.getJSONObject("category");

                                    builder.append(lastObjtoCat.getString("categoryName"));

                                }

                                minicourse.setEnrolled(false);
                                minicourse.setRelevance(builder.toString());

                                JSONObject tutorObj = object.getJSONObject("tutor");
                                Tutor tutor = new Tutor(tutorObj.getInt("id"),tutorObj.getString("name"),tutorObj.getString("description"));
                                tutor.setImgUrl(tutorObj.getString("img"));
                                tutors.add(tutor);

                                minicourse.setTutorName(tutor.getName());
                                minicourse.setTutorImageUrl(tutor.getImgUrl());
                                minicoursesList.add(minicourse);


                            }

                            listAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                            checkForSql();

                        } catch (JSONException e) {


                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LCL_MI)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }

                        progressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(ANError anError) {


                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LCL_MI)")
                                .setTitleText("Oops..!!")
                                .show();
                        progressBar.setVisibility(View.GONE);
                    }
                });


        }

    private void checkForSql() {
//
//        List<Minicourse> list = SugarRecord.findWithQuery(Minicourse.class,"Select * from Minicourse where description LIKE '%question%'");
//
//        Log.d("sqlcheckto", String.valueOf(list.size()));
//
//        if(list.size() > 0){
//
//            for (Minicourse m: list) {
//                Log.d("sqlcheckto", m.getName());
//
//            }
//        }

    }


    private void fetchDataWithFilters(final ArrayList<String> checkedItems) {


        progressBar.setVisibility(View.VISIBLE);


        minicoursesList.clear();
        tutors.clear();

        String generatedStringtoSend = generateRequiredFilterString(checkedItems);

        Log.d("lolololo",generatedStringtoSend);

        if(generatedStringtoSend.isEmpty()){
            fetchData();
            return;
        }


        List<SessionManager> session = listAll(SessionManager.class);


        AndroidNetworking.post(Api_Urls.BASE_URL + "api/minicourses/withFilters")
                .setContentType("application/x-www-form-urlencoded")
                .addStringBody(generatedStringtoSend)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {

                            if(response.length() == 0){

                                chipsCardView.setVisibility(View.GONE);
                                checkedItems.clear();

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame,new NoFilterResultFragment());
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                            for(int i = 0; i < response.length(); ++i){

                                JSONObject object = response.getJSONObject(i);

                                String isActive = object.getString("isActive");


                                if(!isActive.contentEquals("true")){

                                    continue;
                                }

                                Log.d("toing",object.toString());

                                Log.d("ioioio",response.length() + "");

                                int minicourse_id = object.getInt("id");
                                String minicourse_name = object.getString("name");

                                String minicourse_description = object.getString("description");
                                double minicourse_rating = object.getDouble("rating");



                                Minicourse minicourse = new Minicourse();
                                minicourse.setCourse_id(minicourse_id);
                                minicourse.setName(minicourse_name);
                                minicourse.setDescription(minicourse_description);
                                minicourse.setRating((float) minicourse_rating);
                                StringBuilder builder = new StringBuilder();


                                JSONArray categories = object.getJSONArray("minicoursecategories");
                                for(int j = 0; j < categories.length(); ++j){

                                    JSONObject catObject = categories.getJSONObject(j);
                                    JSONObject lastObjtoCat = catObject.getJSONObject("category");

                                    builder.append(lastObjtoCat.getString("categoryName"));

                                }

                                minicourse.setEnrolled(false);
                                minicourse.setRelevance(builder.toString());

                                JSONObject tutorObj = object.getJSONObject("tutor");
                                Tutor tutor = new Tutor(tutorObj.getInt("id"),tutorObj.getString("name"),tutorObj.getString("description"));
                                tutor.setImgUrl(tutorObj.getString("img"));

                                tutors.add(tutor);

                                minicourse.setTutorName(tutor.getName());
                                minicourse.setTutorImageUrl(tutor.getImgUrl());
                                minicoursesList.add(minicourse);



                            }

                            listAdapter.notifyDataSetChanged();

                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("There seems a problem with us.\nPlease try again later.(101LCL_MIF)")
                                    .setTitleText("Oops..!!")
                                    .show();
                        }


                        progressBar.setVisibility(View.GONE);


                    }

                    @Override
                    public void onError(ANError anError) {

                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Connection Error!\nPlease try again later.(202LCL_MIF)")
                                .setTitleText("Oops..!!")
                                .show();

                        progressBar.setVisibility(View.GONE);

                    }
                });
    }

    private String generateRequiredFilterString(ArrayList<String> checkedItems) {

            StringBuilder filterString = new StringBuilder();

            for(int i = 0; i < checkedItems.size(); ++i){

                filterString.append("filter");

                if(checkedItems.get(i).contentEquals("Physics")){

                    filterString.append("[subjectObject][]=" + CONSTANTS.SUBJECT_PHYSICS + "&");
                }

                if(checkedItems.get(i).contentEquals("Chemistry")){

                    filterString.append("[subjectObject][]=" + CONSTANTS.SUBJECT_CHEMISTRY + "&");
                }

                if(checkedItems.get(i).contentEquals("Maths")){

                    filterString.append("[subjectObject][]=" + CONSTANTS.SUBJECT_MATHS + "&");
                }


                if(checkedItems.get(i).contentEquals("JEE Mains")){

                    filterString.append("[categoryObject][]=" + CONSTANTS.CATEGORY_MAINS + "&");
                }

                if(checkedItems.get(i).contentEquals("JEE Advanced")){

                    filterString.append("[categoryObject][]=" + CONSTANTS.CATEGORY_ADVANCED + "&");
                }

                if(checkedItems.get(i).contentEquals("CBSE")){

                    filterString.append("[categoryObject][]=" + CONSTANTS.CATEGORY_CBSE + "&");
                }


                if(checkedItems.get(i).contentEquals("XII")){

                    filterString.append("[classObject][]=" + CONSTANTS.CLASS_12 + "&");
                }

                if(checkedItems.get(i).contentEquals("XI")){

                    filterString.append("[classObject][]=" + CONSTANTS.CLASS_11 + "&");
                }

                if(checkedItems.get(i).contentEquals("X")){

                    filterString.append("[classObject][]=" + CONSTANTS.CLASS_10 + "&");
                }


                if(checkedItems.get(i).contentEquals("Beginner")){

                    filterString.append("[difficultyObject][]=" + "Beginner&");
                }

                if(checkedItems.get(i).contentEquals("Intermediate")){

                    filterString.append("[difficultyObject][]=" + "Intermediate&");
                }

                if(checkedItems.get(i).contentEquals("Advanced")){

                    filterString.append("[difficultyObject][]=" + "Advance&");
                }


                if(checkedItems.get(i).contentEquals("English")){

                    filterString.append("[mediumObject][]=" + "English&");
                }

                if(checkedItems.get(i).contentEquals("Hindi")){

                    filterString.append("[mediumObject][]=" + "Hindi&");
                }

            }

            int index = filterString.lastIndexOf("&");

        if(index >= 0) {
                filterString.deleteCharAt(index);
            }

        return filterString.toString();
    }

    @Override
    public void justCallingFunction(ArrayList<String> chips) {

        if(chips.size() > 0 && chipsCardView.getVisibility() == View.GONE) {

            chipsCardView.setVisibility(View.VISIBLE);
        }
        fetchDataWithFilters(chips);

    }

    //  url = "filter[classObject][]=2&filter[categoryObject][]=1&filter[categoryObject][]=2"

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deleteAll(Tutor.class);
        Log.d("sqlcheckto", "on Destroy View called");
        deleteAll(Minicourse.class);
        ISVISIBLE = false;
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        swipeRefreshLayout.setEnabled( verticalOffset == 0);
    }
}
