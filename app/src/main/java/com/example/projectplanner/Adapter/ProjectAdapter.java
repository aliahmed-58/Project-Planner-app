package com.example.projectplanner.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectplanner.DatabaseHelper;
import com.example.projectplanner.R;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectHolder> {

    private ArrayList<Project> projects = new ArrayList<>();
    Context mContext;
    private onCheckListener onCheckListener;
    DatabaseHelper db;

    public ProjectAdapter(ArrayList<Project> projects,onCheckListener listener, Context mContext) {
        this.projects = projects;
        this.onCheckListener = listener;
        this.mContext = mContext;
        db = new DatabaseHelper(mContext);
    }

    public ProjectHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_project_panel, null);
        return new ProjectHolder(view, onCheckListener);

    }

    public void onBindViewHolder( ProjectAdapter.ProjectHolder holder, int position) {
        holder.projectName.setText(projects.get(position).getName());
        holder.desc.setText(projects.get(position).getDescription());
        holder.date.setText(projects.get(position).getDate());
        holder.daysLeft.setText(projects.get(position).getDaysLeft());
        boolean x = projects.get(position).getStatus() == 1;
        Log.d("STATUS",String.valueOf(x));
        holder.status.setChecked(x);

    }

    public int getItemCount() {
        return projects.size();
    }

    public void updateData(ArrayList<Project> projects1) {
        this.projects = projects1;
        this.notifyDataSetChanged();
    }

    protected class ProjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView projectName;
        TextView desc;
        TextView date;
        TextView daysLeft;
        CheckBox status;
        onCheckListener listener;

        public ProjectHolder(View itemView, onCheckListener listener) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            projectName = itemView.findViewById(R.id.projectName);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            status = itemView.findViewById(R.id.status);
            this.listener = listener;
            daysLeft = itemView.findViewById(R.id.daysLeft);
            status.setOnClickListener(this);
        }

        public void onClick(View v) {
            listener.onStatusChanged(getAdapterPosition());
        }

        public boolean onLongClick(View v) {

            AlertDialog.Builder adb = new AlertDialog.Builder(mContext);

            adb.setTitle("Delete project?");
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    Handler handler = new Handler(Looper.getMainLooper());

                    executor.execute(new Runnable() {
                        public void run() {

                            db.deleteItem(projectName.getText().toString());

                            handler.post(new Runnable() {
                                public void run() {
                                projects.remove(projects.get(getAdapterPosition()));
                                notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            adb.show();

            return true;

        }
    }

    public interface onCheckListener {
        void onStatusChanged(int position);
    }
}
