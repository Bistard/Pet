package com.example.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddGoalActivity extends AppCompatActivity {

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

        // initial datePicker
        startDatePickerButton = findViewById(R.id.startDateButton);
        startDateText = findViewById(R.id.addGoalStartTime);
        startDatePickerDialog = Date.initImageDatePicker(this, startDatePickerDialog, startDatePickerButton, startDateText);

        // TODO: check if the endDate >= startDate
        endDatePickerButton = findViewById(R.id.endDateButton);
        endDateText = findViewById(R.id.addGoalDeadline);
        endDatePickerDialog = Date.initImageDatePicker(this, endDatePickerDialog, endDatePickerButton, endDateText);


        // finish button listener
        Button finishButton = findViewById(R.id.finishCreateGoal);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGoal();
            }
        });

    }

    public void createNewGoal() {
        TextView goalName        = findViewById(R.id.addGoalName);
        TextView goalDescription = findViewById(R.id.addGoalDescription);
        TextView goalStartTime   = findViewById(R.id.addGoalStartTime);
        TextView goalDeadline    = findViewById(R.id.addGoalDeadline);

        User user = User.Initialize();
        // TODO: testing only
        user.addGoal(goalName.toString(),
                     goalDescription.toString(),
                     2021, 3, 6,
                     2021, 3, 30,
                     0
        );


        finish();
    }

    public void openStartDataPicker(View view) {
        startDatePickerDialog.show();
    }
    public void openEndDataPicker(View view) {
        endDatePickerDialog.show();
    }

}