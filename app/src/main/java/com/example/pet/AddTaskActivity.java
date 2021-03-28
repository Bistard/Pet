package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {

    public DatePickerDialog endDatePickerDialog;
    Button editEndDate;

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

        // finish button listener
        Button finishButton = findViewById(R.id.finishCreateTask);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewTask();
            }
        });
    }

    public void createNewTask() {
        // TODO:
        TextView taskName        = findViewById(R.id.addTaskName);
        TextView taskDescription = findViewById(R.id.addTaskDescription);
        TextView taskDeadline    = findViewById(R.id.addTaskDeadline);

        User user = User.Initialize();
        String[] endDate = taskDeadline.getText().toString().split(" ");
        int endYear    = Integer.parseInt(endDate[2]);
        int endMonth   = Date.toNumMonthFormat(endDate[0]);
        int endDay     = Integer.parseInt(endDate[1]);
        // user.addTask();


        finish();
    }





}