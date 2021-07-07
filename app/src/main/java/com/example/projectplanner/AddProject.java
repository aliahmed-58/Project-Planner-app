package com.example.projectplanner;

import androidx.appcompat.app.AppCompatActivity;


import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddProject extends AppCompatActivity {

    private final UmmalquraCalendar hijri = new UmmalquraCalendar();
    private final Calendar greg = Calendar.getInstance();

    DatabaseHelper db;

    EditText projectName;
    EditText description;
    EditText date;
    EditText link;
    Button cancel;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        date = (EditText) findViewById(R.id.date);
        projectName = (EditText) findViewById(R.id.projectName);
        description = (EditText) findViewById(R.id.description);
        add = (Button) findViewById(R.id.save);
        link = findViewById(R.id.link);
        cancel = findViewById(R.id.cancel);

        DatePickerDialog.OnDateSetListener datepickerDialog = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                greg.set(Calendar.YEAR, year);
                greg.set(Calendar.MONTH, month);
                greg.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                hijri.setTime(greg.getTime());
                updateDate();
            }
        };

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatePickerDialog(AddProject.this, datepickerDialog,greg.get(Calendar.YEAR), greg.get(Calendar.MONTH), greg.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (verifyInput()) {
                    onSave();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Cannot have an empty field", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateDate() {
        String mydate = "dd MM yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(mydate, Locale.ENGLISH);

        SimpleDateFormat hijriFormat = new SimpleDateFormat(mydate, Locale.ENGLISH);
        hijriFormat.setCalendar(hijri);

        String finalDate = hijriFormat.format(hijri.getTime()) + " | " + dateFormat.format(greg.getTime());

        date.setText(finalDate);
    }


    private boolean verifyInput() {
        if (dataVerifier(projectName.getText().toString()) && dataVerifier(description.getText().toString()) && dataVerifier(date.getText().toString())) {
            return true;
        }
        return false;
    }

    private boolean dataVerifier(String query) {
        return !query.replaceAll(" ", "").equals("");
    }

    private void onSave() {
        String name = projectName.getText().toString();
        String desc = description.getText().toString();
        String dates = date.getText().toString();
        String project_link = link.getText().toString();

        Boolean res = db.insertData(name, desc, dates, project_link);
        if (res) {
            Toast.makeText(this, "Data saved succesfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "db error", Toast.LENGTH_SHORT).show();

        }
    }
}