package com.learnacad.learnacad.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.learnacad.learnacad.Adapters.NotificationsListAdapter;
import com.learnacad.learnacad.Adapters.RecyclerItemTouchHelper;
import com.learnacad.learnacad.Models.Notifications;
import com.learnacad.learnacad.R;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {



    RecyclerView recyclerView;
    ArrayList<Notifications> notificationsArrayList;
    NotificationsListAdapter notificationsListAdapter;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        Toolbar toolbar = findViewById(R.id.toolbarNotifications);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification Center");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationIcon(R.drawable.leftarrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        recyclerView = findViewById(R.id.notificationsListRecyclerView);
        coordinatorLayout = findViewById(R.id.notificationsListCoordinatorLayout);
        notificationsArrayList = new ArrayList<>();
        notificationsListAdapter = new NotificationsListAdapter(this,notificationsArrayList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(notificationsListAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        fetchData();

    }



    private void fetchData() {
        for(int i = 0; i < 10; i++){

            notificationsArrayList.add(new Notifications("title " + i,"sample description"));
        }

        notificationsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if(viewHolder instanceof NotificationsListAdapter.NotificationsListViewHolder){

            String name = notificationsArrayList.get(viewHolder.getAdapterPosition()).getTitle();

            final Notifications deletedNotification = notificationsArrayList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            notificationsListAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackBar = Snackbar.make(coordinatorLayout, "Notification Cleared!",Snackbar.LENGTH_LONG);

            snackBar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationsListAdapter.restoreItem(deletedNotification,deletedIndex);
                }
            });

            snackBar.setActionTextColor(Color.YELLOW);
            snackBar.show();
        }
    }


}
