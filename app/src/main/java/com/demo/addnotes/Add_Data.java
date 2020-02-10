package com.demo.addnotes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;

public class Add_Data extends AppCompatActivity {

    String str_title, str_notes, str_date, str_time;
    EditText et_title, et_notes, et_date, et_time;
    Button save, rv_btn;
    public static final String DATABASE_NAME = "TaskDatabases.db";
    SQLiteDatabase mDatabase;
    private int mYear, mMonth, mDay;
    TimePickerDialog picker;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__data);


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        Log.d("1_firstStart", "onCreate: " + firstStart);
        if (firstStart) {
            showStartDialog();
            Log.d("2_firstStart", "onCreate: " + firstStart);

        }

        mDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createEmployeeTable();

        et_title = findViewById(R.id.et_title);
        et_notes = findViewById(R.id.et_details);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);

        et_time.setFocusable(false);
        et_date.setFocusable(false);

        et_date.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerFragment df = new DatePickerFragment();

                df.setMinDate(mDay, mMonth, mYear);

                df.show(getSupportFragmentManager(), "DatePicker");
                df.setOnDateSetListener(new DatePickerFragment.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        mYear = year;
                        mDay = dayOfMonth;
                        mMonth = monthOfYear;

                        monthOfYear = monthOfYear + 1;
                        et_date.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
                    }
                });

            }
        });

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(Add_Data.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                et_time.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        rv_btn = findViewById(R.id.viewdata);
        rv_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TaskList.class);
                startActivity(intent);
            }
        });

    }


    private void createEmployeeTable() {

        try {
            mDatabase.execSQL("CREATE TABLE IF NOT EXISTS Task " +
                    "(\n" +
                    "    id INTEGER NOT NULL CONSTRAINT employees_pk PRIMARY KEY AUTOINCREMENT,\n" +
                    "    Title varchar(200) NOT NULL,\n" +
                    "    Notes varchar(200) NOT NULL,\n" +
                    "    Date varchar(200) NOT NULL,\n" +
                    "    Time Varchar(200) NOT NULL,\n" +
                    "    C_TimeStamp Varchar(200) NOT NULL DEFAULT CURRENT_DATE\n" +
                    ");"
            );
        } catch (SQLiteException e) {
            Log.d("CReate", "createEmployeeTable: " + e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_favorite) {

            //Get the Enter data
            str_title = et_title.getText().toString().trim();
            str_notes = et_notes.getText().toString().trim();
            str_date = et_date.getText().toString();
            str_time = et_time.getText().toString();


            if (str_title.isEmpty()) {

                FancyToast.makeText(getApplicationContext(), "Fill the Title", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

            } else {

                String insertSQL = "INSERT INTO Task \n" +
                        "(Title, Notes, Date, Time)\n" +
                        "VALUES \n" +
                        "(?, ?, ?, ?);";

                mDatabase.execSQL(insertSQL, new String[]{str_title, str_notes, str_date, str_time});
                FancyToast.makeText(getApplicationContext(), "Great! Data Saved!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                Intent intent = new Intent(getApplicationContext(), TaskList.class);
                finish();
                startActivity(intent);
                return true;
            }


        }

        return super.

                onOptionsItemSelected(item);
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("One Time Dialog")
                .setMessage("This should only be shown once")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
        Log.d("3_firstStart", "onCreate: " + editor);

    }
}

