package com.demo.addnotes;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> taskList;
    int custom_list_item;
    SQLiteDatabase mDatabase;

    private int mYear, mMonth, mDay;
    TimePickerDialog picker;

    public TaskAdapter(TaskList context, int item_list, List<Task> taskList, SQLiteDatabase mDatabase) {
        this.context = context;
        this.taskList = taskList;
        this.custom_list_item = item_list;
        this.mDatabase = mDatabase;

    }

    @androidx.annotation.NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull TaskViewHolder holder, int position) {
        final Task task = taskList.get(position);
        Log.i("list4", "[" + taskList + "]");

        holder.title.setText(task.getTitle());
        holder.notes.setText(task.getNote());
        holder.date.setText(task.getDate());
        holder.time.setText(task.getTime());
        holder.TimeStamp.setText("Date Of Creation: " + task.getCurrentTime());
        holder.id.setText(task.getId());
        holder.l_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmployee(task);
            }
        });
        holder.l_DButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteTask(task);
            }
        });

    }

    void reloadEmployeesFromDatabase() {
        Cursor cursorproduct1 = mDatabase.rawQuery("SELECT * FROM Task", null);
        Log.d("cursorproduct1", "reloadEmployeesFromDatabase: " + cursorproduct1);
        if (cursorproduct1.moveToFirst()) {
            taskList.clear();
            do {
                taskList.add(new Task(
                        cursorproduct1.getString(0),
                        cursorproduct1.getString(1),
                        cursorproduct1.getString(2),
                        cursorproduct1.getString(3),
                        cursorproduct1.getString(4),
                        cursorproduct1.getString(5)));
            } while (cursorproduct1.moveToNext());
        }
        cursorproduct1.close();
        notifyDataSetChanged();
    }

    private void DeleteTask(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Intent intent = new Intent(context, Add_Data.class);
                String sql = "DELETE FROM Task WHERE id = ?";
                mDatabase.execSQL(sql, new Integer[]{Integer.valueOf(task.getId())});
                reloadEmployeesFromDatabase();
                ((Activity) context).finish();
                context.startActivity(intent);


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateEmployee(final Task task) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_update_employee, null);
        builder.setView(view);


        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editusername = view.findViewById(R.id.editUsername);
        final EditText edittime = view.findViewById(R.id.editTime);
        final EditText editdate = view.findViewById(R.id.editDate);

        edittime.setFocusable(false);
        editdate.setFocusable(false);

        editTextName.setText(task.getTitle());
        editusername.setText(task.getNote());
        edittime.setText(task.getTime());
        editdate.setText(task.getDate());

        editdate.setOnClickListener(new View.OnClickListener() {
            //

            @Override
            public void onClick(View v) {

                final Calendar cal = Calendar.getInstance();
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerFragment df = new DatePickerFragment();

                df.setMinDate(mDay, mMonth, mYear);

                df.show(((FragmentActivity) context).getSupportFragmentManager(), "DatePicker");
                df.setOnDateSetListener(new DatePickerFragment.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        mYear = year;
                        mDay = dayOfMonth;
                        mMonth = monthOfYear;

                        monthOfYear = monthOfYear + 1;
                        editdate.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);

                    }
                });


            }
        });

        edittime.setOnClickListener(new View.OnClickListener() {
            //            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                edittime.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        // CREATE METHOD FOR EDIT THE FORM
        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String username = editusername.getText().toString().trim();
                String Time = edittime.getText().toString().trim();
                String Date = editdate.getText().toString().trim();


                String sql = "UPDATE Task \n" +
                        "SET Title = ?, \n" +
                        "Notes = ?,\n" +
                        "Date = ?,\n" +
                        "Time= ? \n" +
                        "WHERE id = ?;\n";

                mDatabase.execSQL(sql, new String[]{name, username, Date, Time, String.valueOf(task.getId())});

                FancyToast.makeText(context, "Great! Data Updated!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                reloadEmployeesFromDatabase();

                dialog.dismiss();
            }
        });
    }

//    private void updateEmployee(final Task task) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View view = inflater.inflate(R.layout.dialog_update_employee, null);
//        builder.setView(view);
//
//
//
//        final EditText editTextName = view.findViewById(R.id.editTextName);
//        final EditText editusername = view.findViewById(R.id.editUsername);
//        final EditText editemail = view.findViewById(R.id.editEmail);
//        final EditText editphno = view.findViewById(R.id.editTextphno);
//
//        editTextName.setText(task.getTitle());
//        editusername.setText(task.getNote());
//        editemail.setText(task.getTime());
//        editphno.setText(task.getDate());
//
//        editemail.setFocusable(false);
//        editphno.setFocusable(false);
//
//
//        editemail.setOnClickListener(new View.OnClickListener() {
//
//
//            @Override
//            public void onClick(View v) {
//
//                final Calendar cal = Calendar.getInstance();
//                mYear = cal.get(Calendar.YEAR);
//                mMonth = cal.get(Calendar.MONTH);
//                mDay = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerFragment df = new DatePickerFragment();
//
//                df.setMinDate(mDay, mMonth, mYear);
//
//                df.show(((FragmentActivity) context).getSupportFragmentManager(), "DatePicker");
//                df.setOnDateSetListener(new DatePickerFragment.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                        mYear = year;
//                        mDay = dayOfMonth;
//                        mMonth = monthOfYear;
//
//                        monthOfYear = monthOfYear + 1;
//                        editemail.setText("" + year + "-" + monthOfYear + "-" + dayOfMonth);
//
//                    }
//                });
//
//
//            }
//        });
//
//
//
//        final AlertDialog dialog = builder.create();
//        dialog.show();
//
//        // CREATE METHOD FOR EDIT THE FORM
//        view.findViewById(R.id.buttonUpdateEmployee).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name = editTextName.getText().toString().trim();
//                String username = editusername.getText().toString().trim();
//                String email = editemail.getText().toString().trim();
//                String phno = editphno.getText().toString().trim();
//
//
//                String sql = "UPDATE Task \n" +
//                        "SET Title = ?, \n" +
//                        "Notes = ?,\n" +
//                        "Date = ?,\n" +
//                        "Time= ? \n" +
//                        "WHERE id = ?;\n";
//
//                mDatabase.execSQL(sql, new String[]{name, email, username, phno, String.valueOf(task.getId())});
//
//                FancyToast.makeText(context, "Great! Data Updated!", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
//                reloadEmployeesFromDatabase();
//
//                dialog.dismiss();
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView title, notes, date, time, TimeStamp, id;
        Button l_Button, l_DButton;
        Button bt_expand;
        LinearLayout lyt_expand, lyt_parent;

        public TaskViewHolder(@androidx.annotation.NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.l_title);
            notes = itemView.findViewById(R.id.l_notes);
            date = itemView.findViewById(R.id.l_date);
            time = itemView.findViewById(R.id.l_time);
            TimeStamp = itemView.findViewById(R.id.l_timestamp);
            id = itemView.findViewById(R.id.l_id);
            l_Button = itemView.findViewById(R.id.l_Button);
            l_DButton = itemView.findViewById(R.id.l_DButton);
            bt_expand = (Button) itemView.findViewById(R.id.bt_expand);
            lyt_expand = itemView.findViewById(R.id.lyt_expand);
            lyt_parent = itemView.findViewById(R.id.lyt_parent);

            bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lyt_expand.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(lyt_parent, new AutoTransition());
                        lyt_expand.setVisibility(View.VISIBLE);
                        bt_expand.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    } else {
                        TransitionManager.beginDelayedTransition(lyt_parent, new AutoTransition());
                        lyt_expand.setVisibility(View.GONE);
                        bt_expand.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    }
                }
            });

        }
    }

}
