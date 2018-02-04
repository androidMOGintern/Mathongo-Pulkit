package com.learnacad.learnacad.Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Activities.MaterialViewActivity;
import com.learnacad.learnacad.Fragments.LCCMaterialsFragment;
import com.learnacad.learnacad.Models.Material;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;

import java.io.File;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Sahil Malhotra on 10-11-2017.
 */

public class LCCMatereialsAdapeter extends RecyclerView.Adapter<LCCMatereialsAdapeter.MaterialsViewHolder> {

    Context mContext;
    long size;
    boolean isDownloaded = false;
    String path;
    ArrayList<Material> materials;
    LCCMaterialsFragment fragment;
    public boolean enrolled;

    public LCCMatereialsAdapeter(Context context,ArrayList<Material> materials,LCCMaterialsFragment fragment,boolean enrolled){

        this.mContext = context;
        this.materials = materials;
        this.fragment = fragment;
        this.enrolled = enrolled;
    }

    public void isEnrolledChanged(){

        this.enrolled = true;

    }


    @Override
    public LCCMatereialsAdapeter.MaterialsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.lccmaterials_item_layout,parent,false);
        return new MaterialsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LCCMatereialsAdapeter.MaterialsViewHolder holder, int position) {

        final Material m = materials.get(position);

        holder.numberingTextView.setText(position + 1 + "");

        holder.titleTextView.setText(m.getName());





            holder.downloadImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!enrolled) {

                        new SweetAlertDialog(mContext,SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Please Enroll to view the lectures")
                                .setTitleText("Oops...")
                                .show();


                    } else {

                        itemClickListener(m);
                    }
                }
            });




            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(!enrolled){

                        new SweetAlertDialog(mContext,SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Please Enroll to view the lectures")
                                .setTitleText("Oops...")
                                .show();

                    }else{

                        itemClickListener(m);
                    }

                }
            });


    }


    void itemClickListener(Material m){

        Intent intent = new Intent(mContext,MaterialViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Material",m);
        intent.putExtras(bundle);
        intent.putExtra("TO_SHOW","MATERIAL");
        mContext.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return materials.size();
    }

    public class MaterialsViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView numberingTextView;
        ImageButton downloadImageButton;

        public MaterialsViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.lccmaterials_recyclerview_item_titleTextView_layout);
            numberingTextView = itemView.findViewById(R.id.lccmaterials_recyclerview_item_numberingTextView_layout);
            downloadImageButton = itemView.findViewById(R.id.lccmaterials_recyclerview_item_downloadImageView_layout);
        }
    }
}
