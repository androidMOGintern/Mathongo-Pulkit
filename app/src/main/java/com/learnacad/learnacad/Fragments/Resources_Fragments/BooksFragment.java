package com.learnacad.learnacad.Fragments.Resources_Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.learnacad.learnacad.Adapters.Resources_Adapters.Resouces_RecyclerView_Adapter;
import com.learnacad.learnacad.Models.Books_Pdfs;
import com.learnacad.learnacad.Models.Class11_Formula;
import com.learnacad.learnacad.Models.Material;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.orm.SugarRecord.listAll;

/**
 * Created by sahil on 29/12/17.
 */

public class BooksFragment extends Fragment {

    Unbinder unbinder;
    View view;

    @BindView(R.id.resourcesRecyclerView)
    RecyclerView recyclerView;

    ArrayList<Material> books_Materials;
    Resouces_RecyclerView_Adapter adapter;
    ProgressBar progressBar;

    @BindView(R.id.no_Offline_Materials_RelativeLayout)
    RelativeLayout noOfflineMaterialsYet_RelativeLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.resources_list_layout_of_fragments,container,false);
        unbinder = ButterKnife.bind(this,view);

        books_Materials = new ArrayList<>();

        progressBar = view.findViewById(R.id.pb);


        progressBar.setProgress(0);
        progressBar.setIndeterminate(true);


        adapter = new Resouces_RecyclerView_Adapter(getActivity(),books_Materials);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchData();
        getActivity().setTitle("Books");
        return view;
    }


    private void fetchData() {

        if(isConnected()) {


            progressBar.setVisibility(View.VISIBLE);

            books_Materials.clear();
            final Books_Pdfs books_pdfs = new Books_Pdfs();
            final StringBuilder sb = new StringBuilder();


            AndroidNetworking.get(Api_Urls.BASE_URL + "authorize/resources")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                String success = response.getString("success");

                                if(success.contentEquals("true")) {

                                    JSONArray objects = response.getJSONArray("TextBooks");

                                    for (int i = 0; i < objects.length(); ++i) {

                                        String mName = objects.getString(i);

                                        Material material = new Material();
                                        material.setCategory_Level_I("TextBooks");
                                        material.setCategory_Level_II("");
                                        sb.append(mName + ",");
                                        material.setName(mName);

                                        books_Materials.add(material);
                                    }

                                    books_pdfs.setStoredInDB(sb.toString());
                                    SugarRecord.save(books_pdfs);

                                    adapter.notifyDataSetChanged();

                                    progressBar.setVisibility(View.GONE);
                                }else{

                                    progressBar.setVisibility(View.GONE);

                                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                            .setContentText("Resources Coming Soon !")
                                            .setTitleText("Oops...")
                                            .setConfirmText("OK")
                                            .show();
                                }
                            } catch (JSONException e) {
                                progressBar.setVisibility(View.GONE);
                                new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("There occured an error while fetching the resources.\nPlease try again later.(101BF_RB)")
                                        .setTitleText("Oops..!!")
                                        .show();

                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Connection Error!.\nPlease try again later.(202BF_RB)")
                                    .setTitleText("Oops..!!")
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }else{

            List<Books_Pdfs> books_pdfs = listAll(Books_Pdfs.class);

            if(books_pdfs != null && books_pdfs.size() > 0){

                Books_Pdfs books_pdfs1 = books_pdfs.get(0);
                books_Materials.clear();

                String mName = books_pdfs1.getStoredInDB();

                String[] offlinePDFs = mName.split(",");

                if(offlinePDFs.length > 0){

                    for (int i = 0; i < offlinePDFs.length ; ++i) {

                        noOfflineMaterialsYet_RelativeLayout.setVisibility(View.GONE);

                        String offName = offlinePDFs[i];


                        Material material = new Material();
                        material.setCategory_Level_I("TextBooks");
                        material.setCategory_Level_II("");
                        material.setName(offName);

                        books_Materials.add(material);
                    }

                    adapter.notifyDataSetChanged();

                }

            }else{

                noOfflineMaterialsYet_RelativeLayout.setVisibility(View.VISIBLE);
            }
        }
    }


    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
        SugarRecord.deleteAll(Books_Pdfs.class);
    }
}
