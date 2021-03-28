package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

public class AddTaskActivity extends AppCompatActivity {

    public static Task task = null;

    public DatePickerDialog endDatePickerDialog;
    Button editEndDate;

    Spinner goalsSpinner;

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


//        //get the spinner from the xml.
//        Spinner dropdown = findViewById(R.id.goal_spinner);
//        //create a list of items for the spinner.
//        String[] items = new String[]{"Study", "Exercise", "Work", "Others"};
//        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//        //There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        //set the spinners adapter to the previously created one.
//        dropdown.setAdapter(adapter);


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


    @Override
    public void finish() {
        task = null;
        super.finish();
    }

    public static void openAddTaskActivity(Context context, Task t) {
        task = t;
        Intent intent = new Intent(context, AddTaskActivity.class);
        context.startActivity(intent);
    }
}