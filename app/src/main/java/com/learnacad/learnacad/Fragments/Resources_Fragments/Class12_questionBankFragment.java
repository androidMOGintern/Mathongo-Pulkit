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
import com.learnacad.learnacad.Models.Class11_QuestionBank;
import com.learnacad.learnacad.Models.Class12_QuestionBank;
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
 * Created by sahil on 25/12/17.
 */

public class Class12_questionBankFragment extends Fragment {
    Unbinder unbinder;
    View view;

    @BindView(R.id.resourcesRecyclerView)
    RecyclerView recyclerView;


    ProgressBar progressBar;

    ArrayList<Material> class12QB_MaterialsFormula;
    Resouces_RecyclerView_Adapter adapter;

    @BindView(R.id.no_Offline_Materials_RelativeLayout)
    RelativeLayout noOfflineMaterialsYet_RelativeLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.resources_list_layout_of_fragments,container,false);
        unbinder = ButterKnife.bind(this,view);

        class12QB_MaterialsFormula = new ArrayList<>();

        adapter = new Resouces_RecyclerView_Adapter(getActivity(),class12QB_MaterialsFormula);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressBar = view.findViewById(R.id.pb);


        progressBar.setIndeterminate(true);
        progressBar.setProgress(0);

        fetchData();
        return view;
    }
    private void fetchData() {

        if(isConnected()) {


            progressBar.setVisibility(View.VISIBLE);

            class12QB_MaterialsFormula.clear();
            final Class12_QuestionBank class12_questionBank = new Class12_QuestionBank();
            final StringBuilder sb = new StringBuilder();



            AndroidNetworking.get(Api_Urls.BASE_URL + "authorize/resources")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                String success = response.getString("success");

                                if(success.contentEquals("true")) {

                                    JSONObject formula = response.getJSONObject("Question_Bank");
                                    JSONArray class11Materials = formula.getJSONArray("Class_12");

                                    for (int i = 0; i < class11Materials.length(); ++i) {

                                        String mName = class11Materials.getString(i);

                                        Material material = new Material();
                                        material.setName(mName);
                                        material.setCategory_Level_I("Question Bank");
                                        material.setCategory_Level_II("Class XII");
                                        sb.append(mName + ",");
                                        class12QB_MaterialsFormula.add(material);
                                    }

                                    class12_questionBank.setStoredInDB(sb.toString());
                                    SugarRecord.save(class12_questionBank);

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
                                        .setContentText("There occured an error while fetching the resources.\nPlease try again later.(101QBF_RC12)")
                                        .setTitleText("Oops..!!")
                                        .show();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {
                            new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Connection Error!.\nPlease try again later.(202QBF_RC12)")
                                    .setTitleText("Oops..!!")
                                    .show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

        }else{

            List<Class12_QuestionBank> listStored = listAll(Class12_QuestionBank.class);

            if(listStored != null && listStored.size() > 0) {

                Class12_QuestionBank object = listStored.get(0);
                class12QB_MaterialsFormula.clear();

                String mName = object.getStoredInDB();

                String[] offlinePDFs = mName.split(",");

                if (offlinePDFs.length > 0) {

                    noOfflineMaterialsYet_RelativeLayout.setVisibility(View.GONE);


                    for (int i = 0; i < offlinePDFs.length; ++i) {

                        String offName = offlinePDFs[i];


                        Material material = new Material();
                        material.setCategory_Level_I("Question Bank");
                        material.setCategory_Level_II("Class XII");
                        material.setName(offName);

                        class12QB_MaterialsFormula.add(material);
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
        SugarRecord.deleteAll(Class12_QuestionBank.class);
    }
}
