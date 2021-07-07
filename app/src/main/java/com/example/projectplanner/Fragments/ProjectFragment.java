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
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectplanner.Adapter.Project;
import com.example.projectplanner.Adapter.ProjectAdapter;
import com.example.projectplanner.AddProject;
import com.example.projectplanner.DatabaseHelper;
import com.example.projectplanner.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProjectFragment extends Fragment implements ProjectAdapter.onCheckListener {

    Context mContext;
    private int mtotal;
    private int mcompleted;
    private int mprogress;
    ArrayList<Project> projects = new ArrayList<>();
    RecyclerView recyclerView;
    ProjectAdapter adapter;
    DatabaseHelper db;
    View view;
    FloatingActionButton addBtn;

    public void onResume() {
        super.onResume();
        initProjects();
        adapter.updateData(projects);
    }

    public void onStop() {
        super.onStop();
        projects.clear();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.projects, container, false);
        addBtn = view.findViewById(R.id.addProject);

        mContext = view.getContext();
        db = new DatabaseHelper(mContext);

        recyclerView = view.findViewById(R.id.recyclerview);
        adapter = new ProjectAdapter(projects, this, mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        initProjects();

        addBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddProject.class));
            }
        });
        return view;
    }



    private void initProjects() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            public void run() {

                ArrayList<Project> x = new ArrayList<>();

                Cursor res = db.getData();
                if (res.getCount() == 0) {
                    return;
                }

                while (res.moveToNext()) {
                    x.add(new Project(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getInt(5)));
                }

                projects = x;

                handler.post(new Runnable() {
                    public void run() {
                        adapter.updateData(projects);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });


    }

    public void onStatusChanged(int position) {
        boolean x = projects.get(position).getStatus() == 1;
        String name = projects.get(position).getName();
        if (!x) {
            Boolean z = db.setCompleted(name);
            if (z) {
                Toast.makeText(mContext, "Completed", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(mContext, "UnCompleted", Toast.LENGTH_SHORT).show();
            db.setUncompleted(name);
        }
    }


}
