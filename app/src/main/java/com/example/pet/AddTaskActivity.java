package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    public static Task task = null;

    private DatePickerDialog endDatePickerDialog;
    private Button editEndDate;

    private Spinner goalsSpinner;

    private ArrayList<Goal> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        // return button listener
        ImageButton back = findViewById(R.id.returnTask);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // initial datePicker
        editEndDate = findViewById(R.id.addTaskDeadline);
        endDatePickerDialog = Date.initDatePicker(this, endDatePickerDialog, editEndDate);

        // initial goalsSpinner
        goalsSpinner = findViewById(R.id.goalsSpinner);

        User user = User.Initialize();
        int currentYear = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new java.util.Date()));
        int currentMonth = Integer.parseInt(new SimpleDateFormat("M", Locale.getDefault()).format(new java.util.Date()));
        int currentDate = Integer.parseInt(new SimpleDateFormat("dd", Locale.getDefault()).format(new java.util.Date()));

        for (Goal g : user.getGoals()) {
            if (g.endDate >= currentYear * 1000 + currentMonth * 100 + currentDate) {
                items.add(g);
            }
        }
        String[] itemNames = new String[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).name();
        }

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, itemNames);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalsSpinner.setAdapter(aa);


        // finish button listener
        Button finishButton = findViewById(R.id.finishCreateTask);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTask();
            }
        });


        if (task != null) {
            TextView title = findViewById(R.id.addTaskTitle);
            title.setText("Change Existing Task");
            finishButton.setText("Change Task");

            EditText taskName = findViewById(R.id.addTaskName);
            taskName.setText(task.name());
            EditText taskDescription = findViewById(R.id.addTaskDescription);
            taskDescription.setText(task.description());

            goalsSpinner.setSelection(items.indexOf(task.getParentGoal()));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            java.util.Date date = new java.util.Date();
            try {
                date = sdf.parse(task.getParentGoal().startYear() + "/" + task.getParentGoal().startMonth() + "/" + task.getParentGoal().startDay());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            endDatePickerDialog.getDatePicker().setMinDate(date.getTime());
            sdf = new SimpleDateFormat("yyyy/MM/dd");
            date = new java.util.Date();
            try {
                date = sdf.parse(task.getParentGoal().endYear() + "/" + task.getParentGoal().endMonth() + "/" + task.getParentGoal().endDay());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            endDatePickerDialog.getDatePicker().setMaxDate(date.getTime());
            endDatePickerDialog.updateDate(task.Year(),task.Month()-1,task.Day());
            editEndDate.setText(User.month2string(task.Month()) + " " + task.Day() + " " + task.Year());
        }


    }

    public void setTask() {
        TextView taskName = findViewById(R.id.addTaskName);
        TextView TaskDescription = findViewById(R.id.addTaskDescription);
        TextView taskDeadLine = findViewById(R.id.addTaskDeadline);

        Spinner goalsSpinner = findViewById(R.id.goalsSpinner);

        User user = User.Initialize();
        String[] endDate = taskDeadLine.getText().toString().split(" ");
        int endYear = Integer.parseInt(endDate[2]);
        int endMonth = Date.toNumMonthFormat(endDate[0]);
        int endDay = Integer.parseInt(endDate[1]);

        if (task == null) {
            user.addTask(taskName.getText().toString(),
                    TaskDescription.getText().toString(),
                    endYear, endMonth, endDay,
                    "Once",
                    items.get(goalsSpinner.getSelectedItemPosition())
            );
        } else {
            task.changeName(taskName.getText().toString());
            task.changeDescription(TaskDescription.getText().toString());
//            task.changeEventDate(endYear, endMonth, endDay);
//            task.changeRecurringRule();
            task.changeParent(items.get(goalsSpinner.getSelectedItemPosition()));
            user.SaveFiles();
        }

        finish();
    }


    @Override
    public void finish() {
        task = null;
        super.finish();
    }

    public void openEventDataPicker(View view) {
        endDatePickerDialog.show();
    }

    public static void openAddTaskActivity(Context context, Task t) {
        task = t;
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivity(intent);
    }
}