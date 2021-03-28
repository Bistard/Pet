package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddGoalActivity extends AppCompatActivity {

    static Goal goal = null;

    // DatePicker attributes
    public DatePickerDialog startDatePickerDialog;
    public ImageButton startDatePickerButton;
    public TextView startDateText;

    public DatePickerDialog endDatePickerDialog;
    public ImageButton endDatePickerButton;
    public TextView endDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        // return button listener
        ImageButton back = findViewById(R.id.returnGoal);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(goal!=null){
            //TODO:change the hint to the existing information
        }

        // initial datePicker
        /*
        // TODO: datePicker needs to be completed
        startDatePickerButton = findViewById(R.id.startDateButton);
        startDateText = findViewById(R.id.addGoalStartTime);
        startDatePickerDialog = Date.initImageDatePicker(this, startDatePickerDialog, startDatePickerButton, startDateText);
         */

        /*
        endDatePickerButton = findViewById(R.id.endDateButton);
        endDateText = findViewById(R.id.addGoalDeadline);
        endDatePickerDialog = Date.initImageDatePicker(this, endDatePickerDialog, endDatePickerButton, endDateText);
         */


        // finish button listener
        Button finishButton = findViewById(R.id.finishCreateGoal);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGoal();
            }
        });

    }

    @Override
    public void finish() {
        goal = null;
        super.finish();
    }

    public void createNewGoal() {
        TextView goalName = findViewById(R.id.addGoalName);
        TextView goalDescription = findViewById(R.id.addGoalDescription);
        TextView goalStartTime = findViewById(R.id.addGoalStartTime);
        TextView goalDeadline = findViewById(R.id.addGoalDeadline);

        User user = User.Initialize();
        String[] startDate = goalStartTime.getText().toString().split(" ");
        String[] endDate = goalDeadline.getText().toString().split(" ");
        int startYear = Integer.parseInt(startDate[2]);
        int startMonth = Date.toNumMonthFormat(startDate[0]);
        int startDay = Integer.parseInt(startDate[1]);
        int endYear = Integer.parseInt(endDate[2]);
        int endMonth = Date.toNumMonthFormat(endDate[0]);
        int endDay = Integer.parseInt(endDate[1]);

        // TODO: unfinished
        if (goal == null) {
            user.addGoal(goalName.getText().toString(),
                    goalDescription.getText().toString(),
                    startYear, startMonth, startDay,
                    endYear, endMonth, endDay,
                    0
            );
        } else {
            //set existing goal
            user.SaveFiles();
        }

        finish();
    }

    public void openStartDataPicker(View view) {
        startDatePickerDialog.show();
    }

    public void openEndDataPicker(View view) {
        endDatePickerDialog.show();
    }

    public static void openAddGoalActivity(Context context, Goal g) {
        goal = g;
        Intent intent = new Intent(context, AddGoalActivity.class);
        context.startActivity(intent);
    }
}