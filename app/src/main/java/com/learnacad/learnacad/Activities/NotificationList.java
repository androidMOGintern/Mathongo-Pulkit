package com.learnacad.learnacad.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import com.learnacad.learnacad.Adapters.NotificationAdapter;
import com.learnacad.learnacad.Models.Material;
import com.learnacad.learnacad.Models.Messages;
import com.learnacad.learnacad.R;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NotificationList extends AppCompatActivity {


    RecyclerView mRecyclerView;
    NotificationAdapter mNotificationAdapter;
    List<Messages> mList = new ArrayList<>();
    List<Messages> tempList = new ArrayList<>();
    Toolbar toolbar;
    BroadcastReceiver updateUIReciver;
    IntentFilter filter;
    private Paint p = new Paint();
    CoordinatorLayout rootlayout;
    int curpos = 5;
    int lastVisibleItemId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        mRecyclerView = findViewById(R.id.notification_recyclerview);
        toolbar = findViewById(R.id.notification_toolbar);
        rootlayout = findViewById(R.id.notification_rootlayout);
        tempList.addAll(SugarRecord.listAll(Messages.class));
        if (tempList.size() < 5) {
            curpos = tempList.size();
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notifications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        SugarRecord.executeQuery("Update Messages SET seen = 1 where seen != 1");
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemId = linearLayoutManager.findLastVisibleItemPosition();

                if (lastVisibleItemId == mList.size() - 1) {
                    curpos += 5;
                    if (curpos > tempList.size())
                        curpos = tempList.size();
                    mList.clear();
                    for (int i = tempList.size() - 1; i > tempList.size() - curpos - 1; i--)
                        mList.add(tempList.get(i));
                    mNotificationAdapter.notifyDataSetChanged();
                }


            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {

                    final int position = viewHolder.getAdapterPosition();
                    mList.remove(position);
                    mNotificationAdapter.notifyDataSetChanged();
                    Snackbar.make(rootlayout, "Deleted Accidentally ??", BaseTransientBottomBar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mList.clear();
                                    for (int i = tempList.size() - 1; i > tempList.size() - curpos - 1; i--)
                                        mList.add(tempList.get(i));
                                    mNotificationAdapter.notifyDataSetChanged();

                                }
                            }).addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {

                                SugarRecord.delete(mList.get(position));
                            }

                        }
                    }).show();
                }


            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;
                float height = (float) itemView.getBottom() - (float) itemView.getTop();
                float width = height / 3;
                if (dX < 0) {
                    p.setColor(Color.parseColor("#388E3C"));
                    c.drawRect((float) itemView.getRight(), (float) itemView.getTop(), dX,
                            (float) itemView.getBottom(), p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });

        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mList.clear();
        for (int i = tempList.size() - 1; i > tempList.size() - curpos - 1; i--)
            mList.add(tempList.get(i));
        mNotificationAdapter = new NotificationAdapter(this, mList, new NotificationAdapter.NotificationListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick: " + mList.get(position).getIntent());
                if (mList.get(position).getIntent() != null) {
                    Intent resultIntent;
                    resultIntent = new Intent(mList.get(position).getIntent());
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    resultIntent.putExtra("MINICOURSE_ID", mList.get(position).getMinicourse_id());
                    if (mList.get(position).getMaterial_name() != null && mList.get(position).getCategory_level_I() == null) {
                        Material m = new Material(mList.get(position).getMaterial_name(), mList.get(position).getMinicourse_id());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Material", m);
                        resultIntent.putExtras(bundle);
                        resultIntent.putExtra("TO_SHOW", "MATERIAL");
                    }
                    if (mList.get(position).getMaterial_name() != null && mList.get(position).getCategory_level_I() != null) {
                        Material m = new Material(mList.get(position).getMaterial_name(), mList.get(position).getMinicourse_id());
                        Bundle bundle = new Bundle();
                        m.setCategory_Level_I(mList.get(position).getCategory_level_I());
                        if (mList.get(position).getCategory_level_II() != null)
                            m.setCategory_Level_II(mList.get(position).getCategory_level_II());
                        bundle.putSerializable("Material", m);
                        resultIntent.putExtras(bundle);
                        resultIntent.putExtra("TO_SHOW", "RESOURCE");
                    } else {
                        resultIntent.putExtra("PROCESS_ID", mList.get(position).getProcess_id());
                    }
                    startActivity(resultIntent);
                }


            }
        });
        mRecyclerView.setAdapter(mNotificationAdapter);

        filter = new IntentFilter();

        filter.addAction("com.hello.action");

        updateUIReciver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "onReceive: notificationrecieved");
                mList.clear();
                tempList.clear();
                tempList.addAll(SugarRecord.listAll(Messages.class));
                for (int i = tempList.size() - 1; i > tempList.size() - curpos - 1; i--)
                    mList.add(tempList.get(i));
                mNotificationAdapter.notifyDataSetChanged();
            }
        };


    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(updateUIReciver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(updateUIReciver);
    }
}
