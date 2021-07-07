package com.example.projectplanner.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.projectplanner.Adapter.ProjectAdapter;
import com.example.projectplanner.AddProject;
import com.example.projectplanner.DatabaseHelper;
import com.example.projectplanner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Dashboard extends Fragment  {

    ProgressBar progressBar;
    DatabaseHelper db;
    TextView totalProjects;
    TextView lastProject;
    int mtotal;
    int mcompleted;
    int mprogress;
    TextView lastCheckin;
    TextView projectsCompleted;
    Context mContext;
    FloatingActionButton add;

    public void onResume() {
        super.onResume();
        getData();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard, container, false);

        progressBar = view.findViewById(R.id.dashboardProgress);
        lastProject = view.findViewById(R.id.lastProject);
        lastCheckin = view.findViewById(R.id.lastCheckIn);
        projectsCompleted = view.findViewById(R.id.projectsCompleted);
        mContext = view.getContext();
        totalProjects = view.findViewById(R.id.totalProjects);
        add = view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddProject.class));
            }
        });
        db = new DatabaseHelper(mContext);

        getData();

        return view;
    }

    private void getData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            public void run() {

                double total = 0;
                double completed = 0;
                Cursor res = db.getProjectsCompleted();

                if (res.getCount() ==0) completed = 0;
                else {
                    while (res.moveToNext()) {
                        if (res.getInt(0) == 1) {
                            completed++;
                            Log.d("COMPLETED", String.valueOf(completed));
                        }
                        total++;
                        Log.d("TOTAL", String.valueOf(total));
                    }
                }

                double finalProg = 0;
                if (total == 0) mprogress = 0;
                else {
                    finalProg = (completed / total ) * 100;
                }

                mtotal = (int) total;
                mcompleted = (int) completed;
                mprogress = (int) finalProg;


//                Log.d("PROGRESS", String.valueOf(mprogress));

                String name = "", deadline = "";
                Cursor res2 = db.getLastProject();
                while (res2.moveToNext()) {
                    name = res2.getString(1);
                    deadline = res2.getString(3);
                }

                String finalName = name;
                String finalDeadline = deadline;

                handler.post(new Runnable() {
                    public void run() {
                        lastProject.setText(finalName);
                        lastCheckin.setText(finalDeadline);
                        totalProjects.setText(mcompleted  + "/" + mtotal);
                        progressBar.setProgress(mprogress);
                    }
                });
            }
        });
    }

}
