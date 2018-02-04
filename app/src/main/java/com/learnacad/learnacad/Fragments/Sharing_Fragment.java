package com.learnacad.learnacad.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.learnacad.learnacad.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sahil on 29/12/17.
 */

public class Sharing_Fragment extends Fragment {


    Unbinder unbinder;

    View view;

    @BindView(R.id.share_fragment_shareButton)
    ImageButton shareButton;

    @BindView(R.id.share_fragment_facebookButton)
    ImageButton shareFaceBookButton;

    @BindView(R.id.share_fragment_whatsappButton)
    ImageButton sharewhatsAppButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.sharing_fragment_layout,container,false);

        unbinder = ButterKnife.bind(this,view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        sharewhatsAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey I am using MathonGo App for free lectures and resources for IIT JEE and Boards. You can too check them here -  http://bit.ly/2q6YO4k");

                try{

                    startActivity(intent);
                }catch (Exception e){

                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("It seems WhatsApp is not installed on your mobile.Please try again later.")
                            .setTitleText("Oops...")
                            .setConfirmText("OK")
                            .show();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Hey I am using MathonGo App for free lectures and resources for IIT JEE and Boards. You can too check them here -  http://bit.ly/2q6YO4k");

                startActivity(intent);
            }
        });

        shareFaceBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.facebook.katana");
                intent.putExtra(Intent.EXTRA_TEXT, "Hey I am using MathonGo App for free lectures and resources for IIT JEE and Boards. You can too check them here -  http://bit.ly/2q6YO4k");

                try{

                    startActivity(intent);
                }catch (Exception e){

                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("It seems Facebook is not installed on your mobile.Please try again later.")
                            .setTitleText("Oops...")
                            .setConfirmText("OK")
                            .show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
