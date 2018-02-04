package com.learnacad.learnacad.Adapters.Resources_Adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.learnacad.learnacad.Adapters.LCCMatereialsAdapeter;
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
 * Created by sahil on 25/12/17.
 */

public class Resouces_RecyclerView_Adapter extends  RecyclerView.Adapter<Resouces_RecyclerView_Adapter.Resources_RecyclerView_ViewHolder>{


    Context mContext;
    long size;
    boolean isDownloaded = false;
    String path;
    ArrayList<Material> materials;

    public Resouces_RecyclerView_Adapter(Context context,ArrayList<Material> materials){

        mContext = context;
        this.materials = materials;
    }


    @Override
    public Resouces_RecyclerView_Adapter.Resources_RecyclerView_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.resources_list_item_layout,parent,false);
        return new Resouces_RecyclerView_Adapter.Resources_RecyclerView_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Resouces_RecyclerView_Adapter.Resources_RecyclerView_ViewHolder holder, int position) {

        final Material m = materials.get(position);

        holder.numberingTextView.setText(position + 1 + ".");

        holder.titleTextView.setText(" " + m.getName());





        holder.downloadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    itemClickListener(m);

            }
        });




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    itemClickListener(m);


            }
        });

    }

    void itemClickListener(Material m){

        Intent intent = new Intent(mContext,MaterialViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Material",m);
        intent.putExtras(bundle);
        intent.putExtra("TO_SHOW","RESOURCES");
        mContext.startActivity(intent);
    }



    @Override
    public int getItemCount() {
        return materials.size();
    }

    public class Resources_RecyclerView_ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTextView;
        TextView numberingTextView;
        ImageButton downloadImageButton;

        public Resources_RecyclerView_ViewHolder(View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.resources_recyclerview_item_titleTextView_layout);
            numberingTextView = itemView.findViewById(R.id.resources_recyclerview_item_numberingTextView_layout);
            downloadImageButton = itemView.findViewById(R.id.resources_recyclerview_item_downloadImageView_layout);
        }
    }
}
