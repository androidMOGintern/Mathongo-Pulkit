package com.learnacad.learnacad.Activities;

import android.app.DownloadManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.brouding.simpledialog.SimpleDialog;
import com.flurry.android.FlurryAgent;
import com.learnacad.learnacad.Fragments.Sharing_Fragment;
import com.learnacad.learnacad.Models.Material;
import com.learnacad.learnacad.Networking.Api_Urls;
import com.learnacad.learnacad.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MaterialViewActivity extends AppCompatActivity {

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

    PDFViewPager pdfViewPager;
    String title;
    LinearLayout root;
    FrameLayout frameLayout;
    PDFPagerAdapter pdfPagerAdapter;
    Fragment fragment;

    SimpleDialog simpleDialog;

    boolean isDownloaded = false;
    String path;
    ProgressBar progressBar;

    public static final String MIME_TYPE_PDF = "application/pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_view);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        Material material = (Material) bundle.getSerializable("Material");


        root = findViewById(R.id.rootView);

        progressBar = findViewById(R.id.pb);

        frameLayout = findViewById(R.id.content_share);

        String toShow = intent.getStringExtra("TO_SHOW");

        simpleDialog = new SimpleDialog.Builder(this)
                .setContent("Please wait,while the pdf is downloading.")
                .setProgressGIF(R.raw.simple_dialog_progress_default)
                .setCancelable(false)
                .setBtnCancelText("Cancel")
                .setBtnCancelShowTime(30000)
                .build();

        simpleDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                onBackPressed();
            }
        });



        if(toShow.contentEquals("RESOURCES")) {

            itemClickListener_Resources(material);

        }else{

            itemClickListener_Materials(material);
        }

    }

    void itemClickListener_Materials(final Material m){


        if (checkPermission()) {

            FlurryAgent.logEvent("Materials_" + m.getName() + "_Clicked");


            path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS;

            File file = new File(path + "/" + m.getName());

            File fileDownloads = new File(path);

            File[] filesExisting = fileDownloads.listFiles();

            if(filesExisting != null) {

                for (File file1 : filesExisting) {

                    if (String.valueOf(file1).equals(String.valueOf(file))) {

                        isDownloaded = true;
                        break;
                    }
                }

            }

            if (isDownloaded) {

                showPdf(path,m.getName());

            } else {

                FlurryAgent.logEvent("Materials_" + m.getName() + "Downloaded");

                simpleDialog.show();


                AndroidNetworking.download(Api_Urls.BASE_URL + "uploads/" + m.getMinicourseId() + "/" + m.getName(), path, m.getName())
                        .build()
                        .startDownload(new DownloadListener() {
                            @Override
                            public void onDownloadComplete() {
                                Toast.makeText(MaterialViewActivity.this, m.getName() + " downloaded", Toast.LENGTH_SHORT).show();
                                DownloadManager downloadManager = (DownloadManager) MaterialViewActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                                if (downloadManager != null) {
                                    File file = new File(path, m.getName());
                                    showPdf(path,m.getName());
                                    simpleDialog.dismiss();

                                    downloadManager.addCompletedDownload(m.getName(), " ", false, "application/pdf", path, file.length(), true);

                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                                new SweetAlertDialog(MaterialViewActivity.this,SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Connection Error!\nPlease try again later.(202MV_MD)")
                                        .setTitleText("Oops..!!")
                                        .show();
                                simpleDialog.dismiss();

                            }
                        });

            }

        } else {

            new SweetAlertDialog(MaterialViewActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("It seems you haven't given the permission to access the storage.\nPlease give the permission to access the materials.")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + MaterialViewActivity.this.getPackageName()));

                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MaterialViewActivity.this.startActivity(intent);
                        }
                    })
                    .show();
        }
    }


    void itemClickListener_Resources(final Material m){
        if (checkPermission()) {


            if(m.getCategory_Level_I() != null && m.getCategory_Level_II() != null)
            FlurryAgent.logEvent("Resources_" + m.getName() + "_" + m.getCategory_Level_I() + "_" + m.getCategory_Level_II() + "_Clicked");


            path = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DOWNLOADS;

            File file = new File(path + "/" + m.getName());

            File fileDownloads = new File(path);

            File[] filesExisting = fileDownloads.listFiles();

            if(filesExisting != null) {

                for (File file1 : filesExisting) {

                    if (String.valueOf(file1).equals(String.valueOf(file))) {

                        isDownloaded = true;
                        break;
                    }
                }
            }

            if (isDownloaded) {

                showPdf(path, m.getName());

            } else {

                if (m.getCategory_Level_II().isEmpty()) {

                    FlurryAgent.logEvent("Resources_" + m.getName() + "_" + m.getCategory_Level_I() + "_" + "_Clicked");


                    if (isConnected()) {


                        simpleDialog.show();
                        AndroidNetworking.download(Api_Urls.BASE_URL + "resources/" + m.getCategory_Level_I() + "/" + m.getName(), path, m.getName())
                                .build()
                                .startDownload(new DownloadListener() {
                                    @Override
                                    public void onDownloadComplete() {
                                        Toast.makeText(MaterialViewActivity.this , m.getName() + " downloaded", Toast.LENGTH_SHORT).show();
                                        DownloadManager downloadManager = (DownloadManager) MaterialViewActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                                        if (downloadManager != null) {
                                            File file = new File(path, m.getName());
                                            showPdf(path,m.getName());
                                            simpleDialog.dismiss();
                                            downloadManager.addCompletedDownload(m.getName(), " ", false, "application/pdf", path, file.length(), true);

                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                        new SweetAlertDialog(MaterialViewActivity.this,SweetAlertDialog.ERROR_TYPE)
                                                .setConfirmText("Ok")
                                                .setTitleText("Oops...")
                                                .setContentText("There occured some error in downloading the file.\n Please try again later.(202MV_RD)")
                                                .show();
                                        simpleDialog.dismiss();

                                    }
                                });
                    } else {

                        Toast.makeText(MaterialViewActivity.this, "Not downloaded", Toast.LENGTH_SHORT).show();
                        simpleDialog.dismiss();

                    }

                } else {

                    FlurryAgent.logEvent("Resources_" + m.getName() + "_" + m.getCategory_Level_I() + "_" + m.getCategory_Level_II() + "_Clicked");

                    simpleDialog.show();


                    if (isConnected()) {

                        AndroidNetworking.download(Api_Urls.BASE_URL + "resources/" + m.getCategory_Level_I() + "/" + m.getCategory_Level_II() + "/" + m.getName(), path, m.getName())
                                .build()
                                .startDownload(new DownloadListener() {
                                    @Override
                                    public void onDownloadComplete() {
                                        Toast.makeText(MaterialViewActivity.this, m.getName() + " downloaded", Toast.LENGTH_SHORT).show();
                                        DownloadManager downloadManager = (DownloadManager) MaterialViewActivity.this.getSystemService(Context.DOWNLOAD_SERVICE);
                                        if (downloadManager != null) {
                                            File file = new File(path, m.getName());
                                            showPdf(path,m.getName());
                                            simpleDialog.dismiss();
                                            downloadManager.addCompletedDownload(m.getName(), " ", false, "application/pdf", path, file.length(), true);

                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {


                                        new SweetAlertDialog(MaterialViewActivity.this,SweetAlertDialog.ERROR_TYPE)
                                                .setConfirmText("Ok")
                                                .setTitleText("Oops...")
                                                .setContentText("There occured some error in downloading the pdf.\n Please try again later.(202MV_RD)")
                                                .show();
                                        simpleDialog.dismiss();


                                    }
                                });
                    } else {

                        Toast.makeText(MaterialViewActivity.this, "Not downloaded", Toast.LENGTH_SHORT).show();

                        simpleDialog.dismiss();

                    }
                }
            }
        } else {

            new SweetAlertDialog(MaterialViewActivity.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("It seems you haven't given the permission to access the storage.\nPlease give the permission to access the resources.")
                    .setConfirmText("OK")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:" + MaterialViewActivity.this.getPackageName()));

                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            MaterialViewActivity.this.startActivity(intent);
                        }
                    })
                    .show();
        }
    }


    private boolean checkPermission() {

        int readExternal = ContextCompat.checkSelfPermission(this.getApplicationContext(), READ_EXTERNAL_STORAGE);
        int writeExternal = ContextCompat.checkSelfPermission(this.getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return (readExternal == PackageManager.PERMISSION_GRANTED && writeExternal == PackageManager.PERMISSION_GRANTED);
    }

    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }



    private void showPdf(String path, String title) {

        if (Build.VERSION.SDK_INT < 21) {

            File file = new File(path + "/" + title);
            Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
            pdfViewIntent.setDataAndType(Uri.fromFile(file), MIME_TYPE_PDF);
            try {
                startActivity(pdfViewIntent);
            }catch (Exception e){

                Toast toast = Toast.makeText(this, "You may not have proper app for viewing this content", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        } else {

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < title.length(); ++i) {

                if (title.charAt(i) == ' ') {

                    sb.append("%20");
                } else {

                    sb.append(title.charAt(i));

                }
            }

            Log.d("checkPDF", "showPdf: " + sb.toString());


            File file = new File(path + "/" + sb.toString());

            pdfViewPager = new PDFViewPager(this, file.getAbsolutePath());

            updateViews();

            //  setContentView(pdfViewPager);
        }
    }
    private void updateViews() {

        root.removeAllViews();

        root.addView(frameLayout,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        root.addView(pdfViewPager,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(pdfViewPager != null)
            ((PDFPagerAdapter) pdfViewPager.getAdapter()).close();
    }
}
