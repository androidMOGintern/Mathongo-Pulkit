//package com.learnacad.learnacad.Fragments;
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.view.InflateException;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.learnacad.learnacad.R;
//
///**
// * Created by Sahil Malhotra on 04-06-2017.
// */
//
//public class ButtonPanelFragment extends Fragment {
//
//
//    private static View v;
//    private static Home_Fragment home_fragment;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//
//        if(v != null){
//
//            ViewGroup parent = (ViewGroup) v.getParent();
//            if(parent != null){
//
//                parent.removeView(v);
//            }
//        }
//        try{
//
//            v = inflater.inflate(R.layout.button_panel,container,false);
//        }catch (InflateException e){
//
//        }
//
//        return v;
//    }
//
//  /*  public void setPanelButtonClicked(PanelButtonClickListener listener){
//
//        this.listener = listener;
//    }*/
//
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        final Button home = (Button) view.findViewById(R.id.homeButton);
//        final Button library = (Button) view.findViewById(R.id.libraryButton);
//        final Button events = (Button) view.findViewById(R.id.eventsButton);
//        final Button profile = (Button) view.findViewById(R.id.profileButton);
//
//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                home.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.homeblue,0,0);
//                home.setTextColor(Color.parseColor("#006DF0"));
//
//                library.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.bookgray,0,0);
//                library.setTextColor(Color.parseColor("#9e9e9e"));
//
//                events.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.calendargray,0,0);
//                events.setTextColor(Color.parseColor("#9e9e9e"));
//
//                profile.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.accountgray,0,0);
//                profile.setTextColor(Color.parseColor("#9e9e9e"));
//
//
//                if (home_fragment == null) {
//
//                    home_fragment = new Home_Fragment();
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, home_fragment);
//                    ft.addToBackStack(null).commit();
//                } else {
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.replace(R.id.content_frame, home_fragment);
//                    ft.addToBackStack(null).commit();
//                }
//            }
//        });
//
//        library.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                library.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.bookblue,0,0);
//                library.setTextColor(Color.parseColor("#006DF0"));
//
//                home.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.homeicon,0,0);
//                home.setTextColor(Color.parseColor("#9e9e9e"));
//
//                events.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.calendargray,0,0);
//                events.setTextColor(Color.parseColor("#9e9e9e"));
//
//                profile.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.accountgray,0,0);
//                profile.setTextColor(Color.parseColor("#9e9e9e"));
//
//
//
//
//                Toast.makeText(getActivity(), "Library", Toast.LENGTH_SHORT).show();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.content_frame,new Library_Top_Fragment());
//                fragmentTransaction.addToBackStack(null).commit();
//
//            }
//        });
//
//        events.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                events.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.calendarblue,0,0);
//                events.setTextColor(Color.parseColor("#006DF0"));
//
//                library.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.bookgray,0,0);
//                library.setTextColor(Color.parseColor("#9e9e9e"));
//
//                home.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.homeicon,0,0);
//                home.setTextColor(Color.parseColor("#9e9e9e"));
//
//                profile.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.accountgray,0,0);
//                profile.setTextColor(Color.parseColor("#9e9e9e"));
//
//
//
//
//                Toast.makeText(getActivity(), "Events", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                profile.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.accountblue,0,0);
//                profile.setTextColor(Color.parseColor("#006DF0"));
//
//                library.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.bookgray,0,0);
//                library.setTextColor(Color.parseColor("#9e9e9e"));
//
//                events.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.calendargray,0,0);
//                events.setTextColor(Color.parseColor("#9e9e9e"));
//
//                home.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.homeicon,0,0);
//                home.setTextColor(Color.parseColor("#9e9e9e"));
//
//
//
//
//                Toast.makeText(getActivity(), "Profile", Toast.LENGTH_SHORT).show();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.content_frame,new Profile_Fragment());
//                fragmentTransaction.addToBackStack(null).commit();
//
//            }
//        });
//
//
//
//
//    }
//
//    /**
//     * Called when a view has been clicked.
//     *
//     * @param v The view that was clicked.
//     */
// /*   @Override
//    public void onClick(View v) {
//
//        id = v.getId();
//    }
//
//    public interface PanelButtonClickListener{
//
//            void onPanelButtonClicked(int id);
//    }*/
//}
