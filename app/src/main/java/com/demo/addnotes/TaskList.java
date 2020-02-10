package com.demo.addnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TaskList extends AppCompatActivity {
    ArrayList<Task> taskList = new ArrayList<>();

    SQLiteDatabase mDatabase;
    ListView listView;
    TaskAdapter taskAdapter;
    RecyclerView recyclerView;
    FloatingActionButton add_form;
    RelativeLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        emptyView = findViewById(R.id.empty_relative_layout);

        add_form = findViewById(R.id.add_form);
        add_form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Add_Data.class);
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_task);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = openOrCreateDatabase(Add_Data.DATABASE_NAME, MODE_PRIVATE, null);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        showEmployeesFromDatabase();


    }

    private void showEmployeesFromDatabase() {
        Cursor cursorproduct = mDatabase.rawQuery("SELECT * FROM Task", null);
        List<Task> taskList = new ArrayList<>();

        if (cursorproduct.moveToFirst()) {
            do {
                String number = cursorproduct.getColumnName(0);
                Log.d("number", "showEmployeesFromDatabase: " + number);
                taskList.add(new Task(
                        cursorproduct.getString(0),
                        cursorproduct.getString(1),
                        cursorproduct.getString(2),
                        cursorproduct.getString(3),
                        cursorproduct.getString(4),
                        cursorproduct.getString(5)

                ));
            } while (cursorproduct.moveToNext());
        }
        if (taskList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            FancyToast.makeText(getApplicationContext(), "No Task Found!", FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
        } else {
            emptyView.setVisibility(View.INVISIBLE);

        }
        //SELECT strftime('%Y-%m-%d %H:%M:%S', date('now'))
        //closing the cursor
        cursorproduct.close();

        //creating the adapter object
        taskAdapter = new TaskAdapter(this, R.layout.item_list, taskList, mDatabase);

        //adding the adapter to listview
        recyclerView.setAdapter(taskAdapter);

        taskAdapter.reloadEmployeesFromDatabase();  //this method is in prdouctadapter
    }
}

